var game = null;
function Game() {
    this.ctx = null;
    this.world = {};
    this.tank = null;
    this.network = new Network();
    this.scores = null;
    this.messages = null;
    this.isConnected = false;

    this.getCtx = function () {
        return this.ctx;
    };

    this.getWorld = function () {
        return this.world;
    };
}

Game.prototype.reset = function () {
    $.post('/rest/map/reset', {}, function (result) {
        game.network.sendMessage(game.tank.tankId, game.tank.tankId + ' has reseted the game');
    });
};

Game.prototype.setConnected = function (isConnected) {
    document.getElementById('connect').disabled = isConnected;
    document.getElementById('disconnect').disabled = !isConnected;
    document.getElementById('rejoin').disabled = !isConnected;
    document.getElementById('reset').disabled = !isConnected;
    document.getElementById('join').style.visibility = isConnected ? 'visible' : 'hidden';
    game.isConnected = isConnected;
};

Game.prototype.disconnect = function () {
    if (game.network.client != null) {
        game.network.client.disconnect();
    }
    game.setConnected(false);
};

Game.prototype.join = function () {
    var name = document.getElementById('name').value;
    $.post('/rest/tanks/' + name, {}, function (result) {
        if (!game) game = new Game();
        game.tank = new Tank(name);
        document.getElementById('join').style.visibility = 'hidden';
        game.network.sendMessage(game.tank.tankId + ' has joined');
        window.requestAnimationFrame(game.draw);
    });
};

Game.prototype.rejoin = function () {
    var name = document.getElementById('name').value;
    $.post('/rest/tanks/' + name, {}, function (result) {
        game.tank.tankId = name;
        game.sendMessage(game.tank.tankId, tank.tankId + ' has rejoined');
    });
};


Game.prototype.connect = function () {
    var connect;
    (connect = function () {
        // init the canvas size with the client viewport
        $.post('/rest/map/width/' + window.innerWidth + '/height/' + window.innerHeight, {}, null);
        var socket = new SockJS('/ws/tank');
        var stompClient = Stomp.over(socket);
        // turn off the debug messages
        stompClient.debug = null;
        stompClient.connect({}, function (frame) {
            game.setConnected(true);
            stompClient.subscribe('/topic/world', function (message) {
                game.world = JSON.parse(message.body);
                if (game.ctx === null) {
                    var canvas = document.getElementById("canvas"),
                        ctx = canvas.getContext("2d");
                    canvas.width = game.world.map.width;
                    canvas.height = game.world.map.height;
                    game.ctx = ctx;
                }
            });
            game.network.client = stompClient;
        });
    })();
};


Game.prototype.drawRotatedImage = function (image, x, y, angle) {
    var TO_RADIANS = Math.PI / 180;
    this.getCtx().save();
    this.getCtx().translate(x, y);
    this.getCtx().rotate(angle * TO_RADIANS);
    this.getCtx().drawImage(image, -(image.width / 2), -(image.height / 2));
    this.getCtx().restore();
};

Game.prototype.drawWalls = function () {
    var rocksBackground = new Image();
    rocksBackground.src = 'img/rocks_background.jpg';
    var pattern = this.getCtx().createPattern(rocksBackground, 'repeat');
    for (var i = 0; i < this.getWorld().map.walls.length; i++) {
        var wall = this.getWorld().map.walls[i];
        this.getCtx().beginPath();
        this.getCtx().fillStyle = pattern;
        // ctx.fillStyle = '#96775a';
        this.getCtx().fillRect(wall.posX, wall.posY, wall.width, wall.height);
    }
};

Game.prototype.drawTanks = function () {
    var __createTank__ = function (tank) {
        game.tank.tankImg = new Image();
        game.tank.tankImg.onload = function () {
            game.ctx.drawImage(game.tank.tankImg, -50, -50, tank.width, tank.height);
        };
        game.tank.tankImg.src = 'img/tank.png';

        game.tank.turretImg = new Image();
        game.tank.turretImg.onload = function () {
            game.ctx.drawImage(game.tank.turretImg, -50, -50, tank.turret.width, tank.turret.height);
        };
        game.tank.turretImg.src = 'img/tank_turret.png';
    };

    var __drawTank__ = function (tank) {
        if (game.tank.tankImg === null) {
            __createTank__(tank);
        }
        if (tank.isVisibility[game.tank.tankId]) {
            game.drawRotatedImage(game.tank.tankImg, tank.posX, tank.posY, tank.rotation);
            game.drawRotatedImage(game.tank.turretImg, tank.turret.posX, tank.turret.posY, tank.turret.rotation);
        }
    };
    for (var key in this.getWorld().tanks) {
        if (this.getWorld().tanks.hasOwnProperty(key)) {
            __drawTank__(this.getWorld().tanks[key]);
        }
    }
};

Game.prototype.drawBullets = function () {
    for (var key in this.getWorld().bullets) {
        if (this.getWorld().bullets.hasOwnProperty(key)) {
            var bullet = this.getWorld().bullets[key];
            game.ctx.beginPath();
            game.ctx.arc(bullet.posX, bullet.posY, bullet.radius, 0, Math.PI * 2);
            game.ctx.fillStyle = "#412924";
            game.ctx.fill();
            game.ctx.closePath();
        }
    }
};


Game.prototype.draw = function (timestamp) {
    var __executeAction__ = function (key) {
        if (key == 37) {
            game.tank.rotateTankLeft();
        }
        if (key == 38) {
            game.tank.moveTankForward();
        }
        if (key == 39) {
            game.tank.rotateTankRight();
        }
        if (key == 40) {
            game.tank.moveTankBackwards();
        }
        if (key == 83) {
            // 's'
            game.tank.fire();
        }
        if (key == 65) {
            // 'a' - pivot left
            game.tank.pivotTurretLeft();
        }
        if (key == 68) {
            // 'd' - pivot right
            game.tank.pivotTurretRight();
        }
    };

    game.ctx.clearRect(0, 0, game.world.map.width, game.world.map.width);
    if(!game.isConnected) return;
    for (var i in keys) {
        if (!keys.hasOwnProperty(i)) continue;
        __executeAction__(i);
    }
    game.drawBullets();
    game.drawWalls();
    game.drawTanks();
    game.drawMessages();
    game.drawScores();
    window.requestAnimationFrame(game.draw);
};


Game.prototype.drawMessages = function () {
    var __formatTimeElement__ = function (timeElement) {
        return timeElement < 10 ? '0' + timeElement : timeElement;
    };

    if (!game.messages) game.messages = document.getElementById('game-log');

    game.messages.innerHTML = '';
    for (var i = 0; i < game.world.messages.length; i++) {
        var message = game.world.messages[i];
        var time = new Date(message.timestamp);
        game.messages.innerHTML = __formatTimeElement__(time.getHours()) + ':' + __formatTimeElement__(time.getMinutes()) + ':' + __formatTimeElement__(time.getSeconds()) + ' - ' + message.message;
    }
};

Game.prototype.drawScores = function () {
    if (!game.scores) game.scores = document.getElementById('scores');
    game.scores.innerHTML = '';
    for (var key in game.world.tanks) {
        if (game.world.tanks.hasOwnProperty(key)) {
            var tank = game.world.tanks[key];
            var p = document.createElement("P");
            var t = document.createTextNode(tank.id + ':' + tank.killCount + ' kills');
            p.appendChild(t);
            game.scores.appendChild(p);
        }
    }
};


function Network() {
    this.client = null;

    this.getClient = function () {
        return this.client;
    }
}

Network.prototype.sendMoveTankForwardMessage = function (tankId) {
    this.getClient().send("/tank/move/forward", {priority: 0}, JSON.stringify({
        tankId: tankId
    }));
};


Network.prototype.sendMoveTankBackwardMessage = function (tankId) {
    this.getClient().send("/tank/move/backward", {priority: 0}, JSON.stringify({
        tankId: tankId
    }));
};


Network.prototype.sendRotateTankRightMessage = function (tankId) {
    this.getClient().send("/tank/rotate/right", {priority: 0}, JSON.stringify({
        tankId: tankId
    }));
};

Network.prototype.sendRotateTankLeftMessage = function (tankId) {
    this.getClient().send("/tank/rotate/left", {priority: 0}, JSON.stringify({
        tankId: tankId
    }));
};

Network.prototype.sendRotateTankTurretRightMessage = function (tankId) {
    this.getClient().send("/tank/turret/rotate/right", {priority: 0}, JSON.stringify({
        tankId: tankId
    }));
};

Network.prototype.sendRotateTankTurretLeftMessage = function (tankId) {
    this.getClient().send("/tank/turret/rotate/left", {priority: 0}, JSON.stringify({
        tankId: tankId
    }));
};

Network.prototype.sendFireBulletMessage = function (tankId) {
    this.getClient().send("/tank/fire", {priority: 0}, JSON.stringify({
        tankId: tankId
    }));
};

Network.prototype.sendMessage = function (message) {
    this.getClient().send("/message", {priority: 2}, JSON.stringify({
        playerId: game.tank.tankId,
        message: message
    }));
};


function Tank(tankId) {
    this.tankId = tankId;
    this.tankImg = null;
    this.turretImg = null;

    this.getTankId = function () {
        return this.tankId;
    };
}

Tank.prototype.moveTankForward = function () {
    game.network.sendMoveTankForwardMessage(this.getTankId());
};

Tank.prototype.moveTankBackwards = function () {
    game.network.sendMoveTankBackwardMessage(this.getTankId());
};

Tank.prototype.rotateTankLeft = function () {
    game.network.sendRotateTankLeftMessage(this.getTankId());
};

Tank.prototype.rotateTankRight = function () {
    game.network.sendRotateTankRightMessage(this.getTankId());
};

Tank.prototype.pivotTurretLeft = function () {
    game.network.sendRotateTankTurretLeftMessage(this.getTankId());
};

Tank.prototype.pivotTurretRight = function () {
    game.network.sendRotateTankTurretRightMessage(this.getTankId());
};

Tank.prototype.fire = function () {
    game.network.sendFireBulletMessage(this.getTankId());
};

var keys = {};
$(document).ready(function () {
    $(document).keydown(function (e) {
        keys[e.which] = true;

    });
    $(document).keyup(function (e) {
        delete keys[e.which];
    });
});


var init = function () {
    game = new Game();
    game.disconnect();
};
