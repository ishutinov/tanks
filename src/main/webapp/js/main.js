var game = null;
function Game() {
    this.world = {};
    this.tank = null;
    this.tanks = {};
    this.turrets = {};
    this.bullets = {};
    this.network = new Network();
    this.scores = null;
    this.messages = null;
    this.scene = null;
    this.camera = null;
    this.renderer = null;
    this.isConnected = false;

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
        window.requestAnimationFrame(game.render);
        game.drawWalls();
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
            });
            game.network.client = stompClient;
        });
    })();
};

Game.prototype.drawWalls = function () {
    var loader = new THREE.TextureLoader();

    // draw floor
    loader.load('img/background.png', function (texture) {
        var geometry = new THREE.PlaneGeometry(window.innerWidth, window.innerHeight, 1, 1);
        texture.wrapS = texture.wrapT = THREE.RepeatWrapping;
        texture.repeat.set(2, 2);
        var material = new THREE.MeshBasicMaterial({map: texture});
        var floor = new THREE.Mesh(geometry, material);
        floor.receiveShadow = true;
        floor.material.side = THREE.DoubleSide;
        floor.position.z = -25;
        game.scene.add(floor);
    });


    loader.load('img/rocks_background.jpg', function (texture) {
        for (var i = 0; i < game.world.map.walls.length; i++) {
            var wall = game.world.map.walls[i];
            var wallDepth = wall.dept;
            var wallQuality = 1;
            // texture.wrapS = texture.wrapT = THREE.RepeatWrapping;
            // texture.repeat.set(1, 1);
            // var material = new THREE.MeshBasicMaterial({map: texture});
            var material = new THREE.MeshBasicMaterial({color: 0x313131});
            var wallObject = new THREE.Mesh(
                new THREE.CubeGeometry(
                    wall.width,
                    wall.height,
                    wallDepth,
                    wallQuality,
                    wallQuality,
                    wallQuality),
                material);
            game.scene.add(wallObject);
            wallObject.position.x = wall.posX;
            wallObject.position.y = wall.posY;
            wallObject.position.z = 0;
        }
    });


};

Game.prototype.drawTanks = function () {
    var __createTankObject__ = function (tank) {
        var tankQuality = 1;
        var material = new THREE.MeshBasicMaterial({color: 0x800000});
        var tankObject = new THREE.Mesh(
            new THREE.CubeGeometry(
                tank.width,
                tank.height,
                tank.depth,
                tankQuality,
                tankQuality,
                tankQuality),
            material);
        tankObject.castShadow = true;
        tankObject.receiveShadow = false;
        game.tanks[tank.id] = tankObject;
        game.scene.add(tankObject);

        var geometry = new THREE.CylinderGeometry(5, 5, tank.turret.height, 32);
        var material = new THREE.MeshBasicMaterial({color: 0xffff00});
        var tankTurretObject = new THREE.Mesh(geometry, material);
        tankTurretObject.castShadow = true;
        tankTurretObject.receiveShadow = false;
        tankTurretObject.geometry.translate(0, -tank.turret.height / 2, 0);
        game.turrets[tank.id] = tankTurretObject;
        game.scene.add(tankTurretObject);
    };
    var TO_RADIANS = Math.PI / 180;
    var __moveTankObject__ = function (tank) {
        var tankObject = game.tanks[tank.id];
        tankObject.position.x = tank.posX;
        tankObject.position.y = tank.posY;
        tankObject.position.z = 0;
        tankObject.rotation.z = tank.rotation * TO_RADIANS;

        var tankTurretObject = game.turrets[tank.id];
        tankTurretObject.position.x = tank.posX;
        tankTurretObject.position.y = tank.posY;
        tankTurretObject.position.z = 20;
        tankTurretObject.rotation.z = tank.turret.rotation * TO_RADIANS;
    };
    for (var key in this.getWorld().tanks) {
        if (this.getWorld().tanks.hasOwnProperty(key)) {
            var tank = this.getWorld().tanks[key];
            if (!game.tanks[key]) __createTankObject__(tank);
            __moveTankObject__(tank);
        }
    }

    for (var tankKey in game.tanks) {
        if (!this.getWorld().tanks[tankKey]) {
            var tankObject = game.tanks[tankKey];
            var turretObject = game.turrets[tankKey];
            delete game.tanks[tankObject.id];
            delete game.turrets[turretObject.id];
            game.scene.remove(tankObject);
            game.scene.remove(turretObject);
        }
    }
};


Game.prototype.drawBullets = function () {
    var __createBulletObject__ = function (bullet) {
        var geometry = new THREE.SphereGeometry(bullet.radius);
        var material = new THREE.MeshBasicMaterial({color: 0xffff00});
        var bulletObject = new THREE.Mesh(geometry, material);

        game.bullets[bullet.id] = bulletObject;
        game.scene.add(bulletObject);
    };

    for (var key in this.getWorld().bullets) {
        if (this.getWorld().bullets.hasOwnProperty(key)) {
            var bullet = this.getWorld().bullets[key];
            if (!game.bullets[key]) __createBulletObject__(bullet);
            var bulletObject = game.bullets[key];
            bulletObject.position.x = bullet.posX;
            bulletObject.position.y = bullet.posY;
            bulletObject.position.z = 10;
        }
    }

    for (var bulletKey in game.bullets) {
        if (!this.getWorld().bullets[bulletKey]) {
            var bulletObject = game.bullets[bulletKey];
            delete game.bullets[bulletObject.id];
            game.scene.remove(bulletObject);
        }
    }

};


Game.prototype.render = function (timestamp) {
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

    if (!game.isConnected) return;
    for (var i in keys) {
        if (!keys.hasOwnProperty(i)) continue;
        __executeAction__(i);
    }
    game.drawBullets();
    game.drawTanks();
    game.drawMessages();
    game.drawScores();

    game.controls.update();

    game.renderer.render(game.scene, game.camera);
    window.requestAnimationFrame(game.render);

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

Network.prototype.sendStopTankMessage = function (tankId) {
    this.getClient().send("/tank/stop", {priority: 0}, JSON.stringify({
        tankId: tankId
    }));
};

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
    this.getTankId = function () {
        return this.tankId;
    };
}

Tank.prototype.stopTank = function () {
    game.network.sendStopTankMessage(this.getTankId());
};

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
        if (e.keyCode == 38 || e.keyCode == 40) {
            game.tank.stopTank();
        }
    });
});


var init = function () {
    game = new Game();
    game.disconnect();

    game.scene = new THREE.Scene();
    game.camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 2000);
    game.camera.position.z = 500;
    game.renderer = new THREE.WebGLRenderer({antialias: true});
    game.renderer.setSize(window.innerWidth, window.innerHeight);
    document.body.appendChild(game.renderer.domElement);

    // Light
    var light = new THREE.PointLight(0xffffff);
    light.position.set(0, 150, 1000);
    light.castShadow = true;
    light.shadowDarkness = 0.5;
    game.scene.add(light);

    game.controls = new THREE.OrbitControls(game.camera, game.renderer.domElement);
};
