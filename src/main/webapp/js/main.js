var stompClient = null;
var ctx = null;
var world = {};
var tankId = null;
var tankImg = null;

function setConnected(connected) {
    document.getElementById('connect').disabled = connected;
    document.getElementById('disconnect').disabled = !connected;
    document.getElementById('rejoin').disabled = !connected;
    document.getElementById('reset').disabled = !connected;
    document.getElementById('joinGame').style.visibility = connected ? 'visible' : 'hidden';
}

function connect() {
    var socket = new SockJS('/ws/tank');
    stompClient = Stomp.over(socket);
    // turn off the debug messages
    stompClient.debug = null;
    stompClient.connect({}, function (frame) {
        setConnected(true);
        stompClient.subscribe('/topic/world', function (message) {
            world = JSON.parse(message.body);
            if (ctx === null) {
                ctx = createContext(world);
            }
            draw(ctx, world);
        });
    });
}

function reset() {
    $.post('/rest/tanks/reset', {}, function (result) {
        sendMessage(tankId, tankId + ' has reseted the game');
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
}

function joinGame() {
    var name = document.getElementById('name').value;
    $.post('/rest/tanks/' + name, {}, function (result) {
        tankId = name;
        document.getElementById('joinGame').style.visibility = 'hidden';
        sendMessage(tankId, tankId + ' has joined');
    });
}

function rejoin() {
    var name = document.getElementById('name').value;
    $.post('/rest/tanks/' + name, {}, function (result) {
        tankId = name;
        sendMessage(tankId, tankId + ' has rejoined');
    });
}

function sendMessage(tankId, message) {
    stompClient.send("/message", {priority: 2}, JSON.stringify({
        playerId: tankId,
        message: message
    }));
}

function moveTank(tankId, speed, rotation) {
    stompClient.send("/tank/move", {priority: 0}, JSON.stringify({
        tankId: tankId,
        speed: speed,
        rotation: rotation
    }));
}

function fireBullet() {
    stompClient.send("/tank/fire", {priority: 0}, JSON.stringify({
        tankId: tankId
    }));
}

function createContext(world) {
    var canvas = document.getElementById("canvas"),
        ctx = canvas.getContext("2d");
    canvas.width = world.map.width;
    canvas.height = world.map.height;
    return ctx;
}

var TO_RADIANS = Math.PI / 180;
function drawRotatedImage(ctx, image, x, y, angle) {
    ctx.save();
    ctx.translate(x, y);
    ctx.rotate(angle * TO_RADIANS);
    ctx.drawImage(image, -(image.width / 2), -(image.height / 2));
    ctx.restore();
}

function drawWalls(world) {
    for (var i = 0; i < world.map.walls.length; i++) {
        var wall = world.map.walls[i];
        ctx.beginPath();
        ctx.fillStyle = "blue";
        ctx.fillRect(wall.posX, wall.posY, wall.width, wall.height);
    }
}

function drawTanks(world) {
    var createTank = function (tank) {
        tankImg = new Image();
        tankImg.onload = function () {
            ctx.drawImage(tankImg, -50, -50, tank.width, tank.height);
        };
        tankImg.src = 'img/tank.png';
    };
    var drawTank = function (tank) {
        if (tankImg === null) {
            createTank(tank);
        }
        if (tank.isVisibility[tankId]) {
            drawRotatedImage(ctx, tankImg, tank.posX, tank.posY, tank.rotation);
        }
    };
    for (var key in world.tanks) {
        if (world.tanks.hasOwnProperty(key)) {
            drawTank(world.tanks[key]);
        }
    }
}

function drawBullets(world) {
    for (var key in world.bullets) {
        if (world.bullets.hasOwnProperty(key)) {
            var bullet = world.bullets[key];
            ctx.beginPath();
            ctx.arc(bullet.posX, bullet.posY, bullet.radius, 0, Math.PI * 2);
            ctx.fillStyle = "#0095DD";
            ctx.fill();
            ctx.closePath();
        }
    }
}

function draw(ctx, world) {
    ctx.clearRect(0, 0, world.map.width, world.map.width);
    drawBullets(world);
    drawWalls(world);
    drawTanks(world);
    drawMessages(world);
    drawScores(world);
}

function drawMessages(world) {
    $('#gameLog').html('');
    for (var i = 0; i < world.messages.length; i++) {
        var message = world.messages[i];
        var time = new Date(message.timestamp);
        $('#gameLog').append('<p>' + time.getHours() + ':' + time.getMinutes() + ':' + time.getSeconds() + ' - ' + message.message + '</p>');
    }
}

function drawScores(world) {
    $('#scores').html('');
    for (var key in world.tanks) {
        if (world.tanks.hasOwnProperty(key)) {
            var tank = world.tanks[key];
            $('#scores').append(tank.id + ':' + tank.killCount + ' kills  ');
        }
    }
}

function rotateTankLeft() {
    var tank = world.tanks[tankId];
    tank.rotation -= 3;
    moveTank(tankId, 0, tank.rotation);
}

function rotateTankRight() {
    var tank = world.tanks[tankId];
    tank.rotation += 3;
    moveTank(tankId, 0, tank.rotation);
}

function moveTankForward() {
    var tank = world.tanks[tankId];
    if (tank.speed < 2) {
        tank.speed += 0.2;
    }
    moveTank(tankId, tank.speed, tank.rotation);
}

function moveTankBackwards() {
    var tank = world.tanks[tankId];
    tank.speed = -1;
    moveTank(tankId, tank.speed, tank.rotation);
}

function stopTank() {
    var tank = world.tanks[tankId];
    tank.speed = 0;
    moveTank(tankId, tank.speed, tank.rotation);
}

$(document).ready(function () {
    $(document).bind('keydown', function (e) {
        key = e.keyCode;
        if (key == 37) {
            rotateTankLeft();
        } else if (key == 38) {
            moveTankForward();
        } else if (key == 39) {
            rotateTankRight();
        } else if (key == 40) {
            moveTankBackwards();
        } else if (key == 32) {
            fireBullet();
        }
    });
    $(document).bind('keyup', function (e) {
        key = e.keyCode;
        if (key == 38 || key == 40) {
            stopTank();
        }
    });
});