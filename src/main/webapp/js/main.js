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
    document.getElementById('join').style.visibility = connected ? 'visible' : 'hidden';
}

function connect() {
    // init the canvas size with the client viewport
    $.post('/rest/map/width/' + window.innerWidth + '/height/' + window.innerHeight, {}, function (result) {
    });
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

function join() {
    var name = document.getElementById('name').value;
    $.post('/rest/tanks/' + name, {}, function (result) {
        tankId = name;
        document.getElementById('join').style.visibility = 'hidden';
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
    var rocksBackground = new Image();
    rocksBackground.src = 'img/rocks_background.jpg';
    var pattern = ctx.createPattern(rocksBackground, 'repeat');

    for (var i = 0; i < world.map.walls.length; i++) {
        var wall = world.map.walls[i];
        ctx.beginPath();
        ctx.fillStyle = pattern;
        // ctx.fillStyle = '#96775a';
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
            // TODO: round on the server
            drawRotatedImage(ctx, tankImg, Math.round(tank.posX), Math.round(tank.posY), tank.rotation);
            // drawRotatedImage(ctx, tankImg,tank.posX, tank.posY, tank.rotation);
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
            ctx.fillStyle = "#412924";
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
    $('#game-log').html('');
    for (var i = 0; i < world.messages.length; i++) {
        var message = world.messages[i];
        var time = new Date(message.timestamp);
        $('#game-log').html(formatTimeElement(time.getHours()) + ':' + formatTimeElement(time.getMinutes()) + ':' + formatTimeElement(time.getSeconds()) + ' - ' + message.message);
    }
}

function formatTimeElement(timeElement) {
    return timeElement < 10 ? '0' + timeElement : timeElement;
}

function drawScores(world) {
    $('#scores').html('');
    for (var key in world.tanks) {
        if (world.tanks.hasOwnProperty(key)) {
            var tank = world.tanks[key];
            $('#scores').append('<p>' + tank.id + ':' + tank.killCount + ' kills' + '</p>');
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
    tank.speed += 0.4;
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
        }
        if (key == 38) {
            moveTankForward();
        }
        if (key == 39) {
            rotateTankRight();
        }
        if (key == 40) {
            moveTankBackwards();
        }
        if (key == 32) {
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
