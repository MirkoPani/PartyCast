//gyroMinigame
var gyroMinigame = {
    balls: [],
    velocity: 8,
    left: [],
    right: [],
    up: [],
    down: [],
    minigameInstructionText: "",
    clockTimer: {},
    clockseconds: 0,
    clocktext: "",
    isRunning: false,
    colori: ["area_arancio", "area_blu", "area_rosa", "area_crema"],
    areaPlayers: [],
    miniGameChangedCount:0,
    preload: function () {
        // game.gamePhaser.load.image('ball', 'img/atari130xe.png');

        game.gameManager.addEventListener(cast.receiver.games.EventType.PLAYER_DROPPED,
            game.minigameManager.handlePlayerDisconnected);


        game.gameManager.addEventListener(cast.receiver.games.EventType.PLAYER_QUIT,
            game.minigameManager.handlePlayerDisconnected);

        game.gamePhaser.load.image('area_arancio', 'img/area_arancio.png');
        game.gamePhaser.load.image('area_blu', 'img/area_blu.png');
        game.gamePhaser.load.image('area_crema', 'img/area_crema.png');
        game.gamePhaser.load.image('area_rosa', 'img/area_rosa.png');


        game.gameManager.addEventListener(cast.receiver.games.EventType.GAME_MESSAGE_RECEIVED,
            gyroMinigame.handleMessage);

        gyroMinigame.miniGameChangedCount=0;

    },
    create: function () {
        game.gamePhaser.stage.backgroundColor = "#63ce7a";
        game.minigameManager.drawMinigameCountText();
        game.minigameManager.restorePlayers();

        //game.minigameManager.drawHud();
        //TODO SHOW INSTRUCTIONS
        //TODO POINTS TABLE
        //TODO GESTIONE SINGOLA MACCHININA
        //TODO GENERAZIONE AREE IN CUI PASSARE SOPRA
        //TODO EVENTUALE COLLISIONE

        gyroMinigame.clocktext = game.gamePhaser.add.text(game.gamePhaser.world.centerX, 100, '---', game.textStyles.timerStyle);
        gyroMinigame.clocktext.anchor.setTo(0.5, 0.5);
        gyroMinigame.clocktext.fontSize = 60;


        //Prepariamo array
        for (var i = 0; i < game.playerManager.players.length; i++) {
            gyroMinigame.left[i] = false;
            gyroMinigame.right[i] = false;
            gyroMinigame.up[i] = false;
            gyroMinigame.down[i] = false;

            game.playerManager.players[i].sprite.x = 400 * (i + 1);
            game.playerManager.players[i].sprite.y = game.gamePhaser.world.centerY;
            game.playerManager.players[i].sprite.anchor.setTo(0.5, 0.5);

            game.playerManager.players[i].nameTitle.anchor.setTo(0.5, 0);
            game.playerManager.players[i].nameTitle.x = game.playerManager.players[i].sprite.x;
            game.playerManager.players[i].nameTitle.y = game.playerManager.players[i].sprite.y + game.playerManager.players[i].sprite.height / 2;

            //Creazione area
            gyroMinigame.areaPlayers[i] = {};
            gyroMinigame.areaPlayers[i].areaSprite = game.gamePhaser.add.sprite(gyroMinigame.generateRandomPosX(), gyroMinigame.generateRandomPosY(), gyroMinigame.colori[i]);
            gyroMinigame.areaPlayers[i].areaSprite.alpha = 0.4;
            gyroMinigame.areaPlayers[i].areaSprite.anchor.set(0);
            gyroMinigame.areaPlayers[i].areaSprite.visible = false;

            //Porto su il nome e avatar
            game.gamePhaser.world.bringToTop(game.playerManager.players[i].sprite);
            game.gamePhaser.world.bringToTop(game.playerManager.players[i].nameTitle);

        }
        game.gamePhaser.world.bringToTop(gyroMinigame.minigameInstructionText);

        gyroMinigame.clocktimer = game.gamePhaser.time.create(false);
        gyroMinigame.clocktimer.loop(Phaser.Timer.SECOND, gyroMinigame.updateDisplay, this);


        game.gameManager.addEventListener(
            cast.receiver.games.EventType.GAME_MESSAGE_RECEIVED, gyroMinigame.checkMessage);


    },
    update: function () {
        // console.log("update");

        // Prevent ball from escaping outside the stage's boundaries
        var halfWidth = game.playerManager.players[0].sprite.width / 2;
        var halfHeight = game.playerManager.players[0].sprite.height / 2;

        for (var i = 0; i < game.playerManager.players.length && gyroMinigame.isRunning; i++) {
            if (gyroMinigame.left[i]) {
                game.playerManager.players[i].sprite.x -= gyroMinigame.velocity;
                //gyroMinigame.left = false;
            }
            if (gyroMinigame.right[i]) {
                game.playerManager.players[i].sprite.x += gyroMinigame.velocity;
                //gyroMinigame.right = false;
            }
            if (gyroMinigame.up[i]) {
                game.playerManager.players[i].sprite.y -= gyroMinigame.velocity;
                //gyroMinigame.up = false;
            }
            if (gyroMinigame.down[i]) {
                game.playerManager.players[i].sprite.y += gyroMinigame.velocity;
                //gyroMinigame.down = false;
            }


            //Controllo boundaries
            if ((game.playerManager.players[i].sprite.x - halfWidth) < 0) {
                game.playerManager.players[i].sprite.x = halfWidth;
            }
            if ((game.playerManager.players[i].sprite.x + halfWidth) > game.gamePhaser.width) {
                game.playerManager.players[i].sprite.x = game.gamePhaser.width - halfWidth;
            }
            if ((game.playerManager.players[i].sprite.y - halfHeight) < 0) {
                game.playerManager.players[i].sprite.y = halfHeight;
            }
            if ((game.playerManager.players[i].sprite.y + halfHeight) > game.gamePhaser.height) {
                game.playerManager.players[i].sprite.y = game.gamePhaser.height - halfHeight;
            }

            game.playerManager.players[i].nameTitle.x = game.playerManager.players[i].sprite.x - game.playerManager.players[i].sprite.width / 2;
            game.playerManager.players[i].nameTitle.y = game.playerManager.players[i].sprite.y + game.playerManager.players[i].sprite.height / 2;

            //Controlliamo se siamo sopra l'area nostra
            if (game.playerManager.players[i].sprite.x > gyroMinigame.areaPlayers[i].areaSprite.x &&
                game.playerManager.players[i].sprite.x < gyroMinigame.areaPlayers[i].areaSprite.width + gyroMinigame.areaPlayers[i].areaSprite.x
                && game.playerManager.players[i].sprite.y > gyroMinigame.areaPlayers[i].areaSprite.y &&
                game.playerManager.players[i].sprite.y < gyroMinigame.areaPlayers[i].areaSprite.height + gyroMinigame.areaPlayers[i].areaSprite.y
            ) {
                console.log("Collision di: " + game.playerManager.players[i].Name);
                gyroMinigame.changeArea(i);
                game.playerManager.players[i].minigamePoints += 25;
            }

        }


    },
    changeArea: function (i) {
        gyroMinigame.areaPlayers[i].areaSprite.x = gyroMinigame.generateRandomPosX();
        gyroMinigame.areaPlayers[i].areaSprite.y = gyroMinigame.generateRandomPosY();

    },
    checkMessage: function (event) {
        // console.log("checkmessage chiamato message= " + event.requestExtraMessageData);
        var message = event.requestExtraMessageData;

        var id = event.playerInfo.playerId;
        var pos = game.playerManager.getPlayerArrayPosFromCastId(id);

        if (message.hasOwnProperty("movUpDown")) {
            switch (message.movUpDown) {
                case 'Up':
                    gyroMinigame.up[pos] = true;
                    gyroMinigame.down[pos] = false;
                    //   console.log("up true");
                    break;
                case 'Down':
                    gyroMinigame.down[pos] = true;
                    // console.log("down true");
                    gyroMinigame.up[pos] = false;
                    break;
                default:
                    gyroMinigame.up[pos] = false;
                    gyroMinigame.down[pos] = false;
                    break;
            }

        }

        if (message.hasOwnProperty("movLeftRight")) {
            switch (message.movLeftRight) {
                case 'Left':
                    gyroMinigame.left[pos] = true;
                    gyroMinigame.right[pos] = false;
                    //   console.log("left true");
                    break;
                case 'Right':
                    gyroMinigame.right[pos] = true;
                    gyroMinigame.left[pos] = false;
                    //  console.log("right true");
                    break;
                default:
                    gyroMinigame.right[pos] = false;
                    gyroMinigame.left[pos] = false;
                    break;
            }

        }

    },
    shutdown: function () {
        game.gameManager.removeEventListener(
            cast.receiver.games.EventType.GAME_MESSAGE_RECEIVED, gyroMinigame.checkMessage);

        game.gameManager.removeEventListener(cast.receiver.games.EventType.PLAYER_DROPPED, game.minigameManager.handlePlayerDisconnected);
        game.gameManager.removeEventListener(cast.receiver.games.EventType.PLAYER_QUIT, game.minigameManager.handlePlayerDisconnected);

        for (var i = 0; i < game.playerManager.players.length; i++) {
            game.gamePhaser.world.remove(game.playerManager.players[i].sprite);
            game.gamePhaser.world.remove(game.playerManager.players[i].shadow);
            game.gamePhaser.world.remove(game.playerManager.players[i].nameTitle);
        }


        game.gameManager.removeEventListener(cast.receiver.games.EventType.GAME_MESSAGE_RECEIVED, gyroMinigame.handleMessage);


    },
    showInstructions: function (instructionString) {

        gyroMinigame.minigameInstructionText = game.gamePhaser.add.text(game.gamePhaser.world.centerX, game.gamePhaser.world.centerY, instructionString, game.textStyles.titleStyle);
        gyroMinigame.minigameInstructionText.fontSize = 80;
        gyroMinigame.minigameInstructionText.anchor.setTo(0.5, 0.5);

        game.gamePhaser.world.bringToTop(gyroMinigame.minigameInstructionText);

        //  Create our Timer
        timer = game.gamePhaser.time.create(false);

        //  Set a TimerEvent to occur after 3 seconds
        timer.add(3000, function () {
            gyroMinigame.minigameInstructionText.setText("3");
            //Avvisiamo i giocatori dei colori
            for (var i = 0; i < game.playerManager.players.length; i++) {
                var message = {color: gyroMinigame.colori[i]};
                game.gameManager.sendGameMessageToPlayer(game.playerManager.players[i].Id, message);
                console.log("Mandato mess a id: "+game.playerManager.players[i].Id+" - "+message );
            }
        }, this);
        timer.add(4000, function () {
            gyroMinigame.minigameInstructionText.setText("2");

        }, this);
        timer.add(5000, function () {
            gyroMinigame.minigameInstructionText.setText("1");
        }, this);
        timer.add(6000, function () {
            gyroMinigame.minigameInstructionText.setText("GO!");
            game.gamePhaser.add.tween(gyroMinigame.minigameInstructionText).to({alpha: 0}, 250, "Linear", true);

            //Notifichiamo che inizia il gioco
            game.gameManager.updateGameplayState(cast.receiver.games.GameplayState.RUNNING, false);
            gyroMinigame.isRunning = true;
            //Facciamo partire timer
            this.initClock();

            //Mettiamo aree visibili
            for (var i = 0; i < game.playerManager.players.length; i++) {
                gyroMinigame.areaPlayers[i].areaSprite.visible = true;
            }


        }, this);

        timer.start();
    },
    stopClock: function () {

        gyroMinigame.clocktimer.stop(false);
        gyroMinigame.clocktext.text = "Tempo fermato";
        console.log('stopClock - Tempo fermato');
    },
    initClock: function () {
        gyroMinigame.clockseconds = 30 + 1; // set countdown seconds, +1 because initial display will also decrease with 1
        this.updateDisplay(); // initial display
        gyroMinigame.clocktimer.start();
    },
    updateDisplay: function () {
        // count down seconds
        gyroMinigame.clockseconds = gyroMinigame.clockseconds - 1;

        // check if time is up
        if (gyroMinigame.clockseconds <= 0) {
            // ohnoes!
            gyroMinigame.stopClock();
            console.log('updateClock - Tempo scaduto');
            gyroMinigame.clocktext.text = 'Tempo scaduto!';


            this.endGame();

        } else {

            // update display
            var minutes = Math.floor(gyroMinigame.clockseconds / 60);
            var seconds = (gyroMinigame.clockseconds - minutes * 60);
            gyroMinigame.clocktext.text = "Tempo rimasto " + (("0" + minutes).substr(-2) + ":" + ("0" + seconds).substr(-2));

        }

    },
    endGame: function () {
        gyroMinigame.isRunning = false;
        //Evitiamo che si muovano
        for (var i = 0; i < game.playerManager.players.length; i++) {
            gyroMinigame.left[i] = false;
            gyroMinigame.right[i] = false;
            gyroMinigame.up[i] = false;
            gyroMinigame.down[i] = false;
            gyroMinigame.areaPlayers[i].areaSprite.visible = false;
            game.playerManager.players[i].sprite.anchor.setTo(0.5, 1);
            game.playerManager.players[i].nameTitle.anchor.setTo(0, 1);
        }

        game.minigameManager.showPointsTable();

    },
    generateRandomPosX: function () {
        return Math.floor(Math.random() * (game.gamePhaser.world.centerX * 2 - 500 + 1) + 250);
    },
    generateRandomPosY: function () {
        return Math.floor(Math.random() * (game.gamePhaser.world.centerY * 2 - 200 + 1) + 100);
    },
    handleMessage: function (event) {


        var message = event.requestExtraMessageData;

        if (message.hasOwnProperty('minigamechanged')) {
            gyroMinigame.miniGameChangedCount++;
            if (gyroMinigame.miniGameChangedCount === game.playerManager.players.length) {
                console.log("Ricevute tutte le conferme.");
                gyroMinigame.showInstructions("Gira il telefono!");
            }
        }

    }

}