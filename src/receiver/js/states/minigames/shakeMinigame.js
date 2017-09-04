//shakeMinigame
var shakeMinigame = {
    shakeText: "",
    idAttuale: "",
    minigameInstructionText: "",
    clockTimer: {},
    clockseconds: 0,
    clocktext: "",
    myLoop:{},
    preload: function () {

        //Player uscito involontariamente oppure ha chiuso l'app
        //Player uscito volontariamente
        game.gameManager.addEventListener(cast.receiver.games.EventType.PLAYER_DROPPED,
            game.minigameManager.handlePlayerDisconnected);


        //Player uscito volontariamente
        game.gameManager.addEventListener(cast.receiver.games.EventType.PLAYER_QUIT,
            game.minigameManager.handlePlayerDisconnected);


    },
    create: function () {
        game.gamePhaser.stage.backgroundColor = "#ce4e29";
        game.minigameManager.drawHud();
        shakeMinigame.drawPoints();
        shakeMinigame.showInstructions("Shake!");


        shakeMinigame.shakeText = game.gamePhaser.add.text(game.gamePhaser.world.centerX, game.gamePhaser.world.centerY, "", game.textStyles.titleStyle);
        shakeMinigame.shakeText.fontSize = 30;
        shakeMinigame.shakeText.anchor.setTo(0.5, 0.5);


        // timer object, note timer won't start running yet

        shakeMinigame.clocktext = game.gamePhaser.add.text(game.gamePhaser.world.centerX, 100, '---', game.textStyles.timerStyle);
        shakeMinigame.clocktext.anchor.setTo(0.5, 0.5);
        shakeMinigame.clocktext.fontSize = 60;

        shakeMinigame.clocktimer = game.gamePhaser.time.create(false);
        shakeMinigame.clocktimer.loop(Phaser.Timer.SECOND, shakeMinigame.updateDisplay, this);

        game.gameManager.addEventListener(
            cast.receiver.games.EventType.GAME_MESSAGE_RECEIVED, shakeMinigame.checkShake);


    },
    newShake: function () {
        shakeMinigame.shakeText.alpha = 0;
        var player = shakeMinigame.getRandomPlayer();

        shakeMinigame.idAttuale = player.id;

        var positionX = shakeMinigame.getRandomPositionX();
        var positionY = shakeMinigame.getRandomPositionY();
        console.log("Posizione: " + positionX + " , " + positionY);
        shakeMinigame.shakeText.setText(player.name);
        shakeMinigame.shakeText.position.y = positionY;
        shakeMinigame.shakeText.position.x = positionX;
        shakeMinigame.shakeText.alpha = 1;

    },
    shutdown: function () {
        console.log("ShakeMinigame-shutdown");
        for (var i = 0; i < game.playerManager.players.length; i++) {
            game.gamePhaser.world.remove(game.playerManager.players[i].sprite);
            game.gamePhaser.world.remove(game.playerManager.players[i].shadow);
            game.gamePhaser.world.remove(game.playerManager.players[i].nameTitle);
        }
        game.gameManager.removeEventListener(cast.receiver.games.EventType.PLAYER_DROPPED, game.minigameManager.handlePlayerDisconnected);
        game.gameManager.removeEventListener(cast.receiver.games.EventType.PLAYER_QUIT, game.minigameManager.handlePlayerDisconnected);

    },
    getRandomPlayer: function () {
        var id = Math.floor(Math.random() * game.playerManager.players.length);
        return {name: game.playerManager.players[id].Name, id: game.playerManager.players[id].Id};
    },
    getRandomPositionX: function () {
        return Math.floor(Math.random() * (game.gamePhaser.world.centerX * 2 - 500 + 1) + 200);
    },
    getRandomPositionY: function () {
        return Math.floor(Math.random() * (game.gamePhaser.world.centerY * 2 - 500 + 1) + 200);
    },
    drawPoints: function () {
        for (var i = 0; i < game.playerManager.players.length; i++) {
            game.playerManager.players[i].pointsText = game.gamePhaser.add.text((window.innerWidth / 5) * (i + 1), window.innerHeight - (window.innerHeight / 25) - 100, "", game.textStyles.titleStyle);

            game.playerManager.players[i].pointsText.anchor.setTo(0.5, 0.5);
            game.playerManager.players[i].minigamePoints = 0;
        }
    },
    checkShake: function (event) {
        console.log("checkShake chiamato message= " + event.requestExtraMessageData);
        var message = event.requestExtraMessageData;

        if (message.hasOwnProperty('shake')) {
            console.log("Abbiamo shake property");
            //ha fatto giusto
            if (event.playerInfo.playerId === shakeMinigame.idAttuale) {
                console.log("Stesso id");
                shakeMinigame.aggiungiPunti(event.playerInfo.playerId);
            }
            else {
                console.log("Id diverso");
                shakeMinigame.togliPunti(event.playerInfo.playerId);
            }
        }
    },
    aggiungiPunti: function (id) {
        console.log("Aggiungo punti");
        var i = game.playerManager.getPlayerArrayPosFromCastId(id);
        game.playerManager.players[i].minigamePoints += 10;
        game.playerManager.players[i].pointsText = game.gamePhaser.add.text((window.innerWidth / 5) * (i + 1), window.innerHeight - (window.innerHeight / 25) - 100, "+10", game.textStyles.timerStyle);
        game.playerManager.players[i].pointsText.fontSize = 40;
        game.playerManager.players[i].pointsText.alpha = 1;

        game.gamePhaser.add.tween(game.playerManager.players[i].pointsText).to({alpha: 0}, 600, "Linear", true);
    },
    togliPunti: function (id) {

        console.log("Tolgo punti");
        var i = game.playerManager.getPlayerArrayPosFromCastId(id);
        game.playerManager.players[i].minigamePoints -= 10;
        game.playerManager.players[i].pointsText = game.gamePhaser.add.text((window.innerWidth / 5) * (i + 1), window.innerHeight - (window.innerHeight / 25) - 100, "-10", game.textStyles.errorStyle);
        game.playerManager.players[i].pointsText.fontSize = 40;
        game.playerManager.players[i].pointsText.alpha = 1;

        game.gamePhaser.add.tween(game.playerManager.players[i].pointsText).to({alpha: 0}, 600, "Linear", true);
    },
    showInstructions: function (instructionString) {

        shakeMinigame.minigameInstructionText = game.gamePhaser.add.text(game.gamePhaser.world.centerX, game.gamePhaser.world.centerY, instructionString, game.textStyles.titleStyle);
        shakeMinigame.minigameInstructionText.fontSize = 80;
        shakeMinigame.minigameInstructionText.anchor.setTo(0.5, 0.5);

        //  Create our Timer
        timer = game.gamePhaser.time.create(false);

        //  Set a TimerEvent to occur after 3 seconds
        timer.add(3000, function () {
            shakeMinigame.minigameInstructionText.setText("3");
        }, this);
        timer.add(4000, function () {
            shakeMinigame.minigameInstructionText.setText("2");
        }, this);
        timer.add(5000, function () {
            shakeMinigame.minigameInstructionText.setText("1");
        }, this);
        timer.add(6000, function () {
            shakeMinigame.minigameInstructionText.setText("GO!");
            game.gamePhaser.add.tween(shakeMinigame.minigameInstructionText).to({alpha: 0}, 250, "Linear", true);

            game.gameManager.sendGameMessageToAllConnectedPlayers({startGame: "1"});
            shakeMinigame.myLoop= game.gamePhaser.time.events.loop(Phaser.Timer.SECOND * 2, shakeMinigame.newShake, this);

            //Facciamo partire timer
            this.initClock();


        }, this);

        timer.start();


    },
    stopClock: function () {

        shakeMinigame.clocktimer.stop(false);
        shakeMinigame.clocktext.text = "Tempo fermato";
        console.log('stopClock - Tempo fermato');
    },
    initClock: function () {
        shakeMinigame.clockseconds = 30 + 1; // set countdown seconds, +1 because initial display will also decrease with 1
        this.updateDisplay(); // initial display
        shakeMinigame.clocktimer.start();
    },
    updateDisplay: function () {
        // count down seconds
        shakeMinigame.clockseconds = shakeMinigame.clockseconds - 1;

        // check if time is up
        if (shakeMinigame.clockseconds <= 0) {
            // ohnoes!
            shakeMinigame.stopClock();
            console.log('updateClock - Tempo scaduto');
            shakeMinigame.clocktext.text = 'Tempo scaduto!';


            this.endGame();

        } else {

            // update display
            var minutes = Math.floor(shakeMinigame.clockseconds / 60);
            var seconds = (shakeMinigame.clockseconds - minutes * 60);
            shakeMinigame.clocktext.text = "Tempo rimasto " + (("0" + minutes).substr(-2) + ":" + ("0" + seconds).substr(-2));

        }

    },
    endGame:function(){
        //Finito il gioco
        game.gamePhaser.time.events.remove(shakeMinigame.myLoop);

        game.gameManager.removeEventListener(cast.receiver.games.EventType.GAME_MESSAGE_RECEIVED, shakeMinigame.checkShake);


        game.minigameManager.showPointsTable();

    }

}