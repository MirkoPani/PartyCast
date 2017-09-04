//touchMinigame
var touchMinigame = {
    indCorretto: -1,
    giocatoriCheHannoGuessato: 0,
    artistId: 0,
    clockTimer: {},
    clockseconds: 0,
    clocktext: "",
    minigameInstructionText: "",
    turnNumber: 0,
    preload: function () {

        //Player uscito involontariamente oppure ha chiuso l'app
        //Player uscito volontariamente
        game.gameManager.addEventListener(cast.receiver.games.EventType.PLAYER_DROPPED,
            game.minigameManager.handlePlayerDisconnected);


        //Player uscito volontariamente
        game.gameManager.addEventListener(cast.receiver.games.EventType.PLAYER_QUIT,
            game.minigameManager.handlePlayerDisconnected);


    },
    showInstructions: function (instructionString) {

        touchMinigame.minigameInstructionText = game.gamePhaser.add.text(game.gamePhaser.world.centerX, game.gamePhaser.world.centerY, instructionString, game.textStyles.titleStyle);
        touchMinigame.minigameInstructionText.fontSize = 80;
        touchMinigame.minigameInstructionText.anchor.setTo(0.5, 0.5);

        //  Create our Timer
        timer = game.gamePhaser.time.create(false);

        //  Set a TimerEvent to occur after 3 seconds
        timer.add(3000, function () {
            touchMinigame.minigameInstructionText.setText("3");
        }, this);
        timer.add(4000, function () {
            touchMinigame.minigameInstructionText.setText("2");
        }, this);
        timer.add(5000, function () {
            touchMinigame.minigameInstructionText.setText("1");
        }, this);
        timer.add(6000, function () {
            touchMinigame.minigameInstructionText.setText("GO!");
            game.gamePhaser.add.tween(touchMinigame.minigameInstructionText).to({alpha: 0}, 250, "Linear", true);

            //griglia visibile
            var wrapper = document.getElementById("wrapper");
            wrapper.style.visibility = "visible";

            //Facciamo partire timer
            this.initClock();

        }, this);

        timer.start();


    },
    create: function () {
        game.gamePhaser.stage.backgroundColor = "#3db3ce";
        game.minigameManager.drawHud();
        this.showInstructions("Artisti e indovini");

        //Scritta Sopra
        var artistTitle = game.gamePhaser.add.text(game.gamePhaser.world.centerX, 80, "In attesa", game.textStyles.titleStyle);
        artistTitle.anchor.setTo(0.5, 0.5);
        artistTitle.fontSize = 60;

        // timer object, note timer won't start running yet

        touchMinigame.clocktext = game.gamePhaser.add.text(game.gamePhaser.world.centerX, 160, '---', game.textStyles.timerStyle);
        touchMinigame.clocktext.anchor.setTo(0.5, 0.5);
        touchMinigame.clocktext.fontSize = 60;

        touchMinigame.clocktimer = game.gamePhaser.time.create(false);

        touchMinigame.clocktimer.loop(Phaser.Timer.SECOND, touchMinigame.updateDisplay, this);

        //Eventi
        game.gameManager.addEventListener(
            cast.receiver.games.EventType.GAME_MESSAGE_RECEIVED, function (event) {
                console.log("Messaggio ricevuto");
                var message = event.requestExtraMessageData;
                //    console.log("message=" + message);


                //Cambiato artista, ricevuto quello nuovo
                if (message.artist) {
                    console.log("Ricevuto message artista");
                    console.log(message.artist + " sta disegnando!");
                    artistTitle.setText(game.playerManager.getPlayerName(message.artist) + " sta disegnando..");
                    artistId = event.playerInfo.playerId;
                }

                //Ci è stato mandato un guess
                if (message.hasOwnProperty('guess')) {
                    console.log("Messaggio guess");
                    touchMinigame.giocatoriCheHannoGuessato++;
                    //il player ha indovinato
                    if (message.guess == touchMinigame.indCorretto) {
                        //Aggiungiamo un punto se ha fatto giusto
                        console.log("Il giocatore con id: " + event.playerInfo.playerId + " ha fatto giusto.");
                        game.playerManager.addPointsTo(event.playerInfo.playerId, 1);
                    }
                    //Il player ha sbagliato
                    else {
                        console.log("Il giocatore con id: " + event.playerInfo.playerId + " ha ciccato.")
                    }

                    touchMinigame.checkAllGuessed();

                }

                //Ci è stata mandata la collezione di parole, inviamola a tutti
                if (message.words) {
                    console.log("Ricevuto message words");
                    game.gameManager.sendGameMessageToAllConnectedPlayers(message);

                    //salviamo l'indice della parola giusta
                    console.log("Indice giusto: " + message.index);
                    touchMinigame.indCorretto = message.index;

                    //salviamo il numero di turno
                    touchMinigame.turnNumber = message.turn;
                    console.log("Numero turno: " + touchMinigame.turnNumber);
                }

                //Caso in cui abbiamo colorato qualcosa
                if (message.grid) {
                    console.log("Ricevuto message grid");
                    var element = document.getElementById(message.grid);
                    if (element) {
                        element.style.backgroundColor = message.color;
                    }
                }

                //Caso per pulizia
                if (message.clear) {
                    console.log("Ricevuto message clear");
                    var tds = document.getElementsByTagName('td');
                    for (var i = 0, td = tds.length; i < td; i++) {
                        tds[i].style.backgroundColor = 'black';
                    }
                }

            });

    },
    shutdown: function () {
        console.log("shutdown");

        for (var i = 0; i < game.playerManager.players.length; i++) {
            game.gamePhaser.world.remove(game.playerManager.players[i].sprite);
            game.gamePhaser.world.remove(game.playerManager.players[i].shadow);
            game.gamePhaser.world.remove(game.playerManager.players[i].nameTitle);
        }

        game.gameManager.removeEventListener(cast.receiver.games.EventType.PLAYER_DROPPED, game.minigameManager.handlePlayerDisconnected);
        game.gameManager.removeEventListener(cast.receiver.games.EventType.PLAYER_QUIT, game.minigameManager.handlePlayerDisconnected);

    },
    checkAllGuessed: function () {
        //Hanno guessato tutti. Finiamo il turno
        if (touchMinigame.giocatoriCheHannoGuessato >= game.playerManager.players.length - 1) {
            console.log("Tutti hanno guessato.");
            this.changeTurn();
        }

    },
    //Funzione chiamata per terminare il turno e farne iniziare uno nuovo
    changeTurn: function () {
        console.log("Changeturn");

        //resettiamo il num di guess
        touchMinigame.giocatoriCheHannoGuessato = 0;

        //stoppiamo timer
        this.stopClock();


        //Nascondiamo la griglia per il touchminigame
        var wrapper = document.getElementById("wrapper");
        wrapper.style.visibility = "hidden";
        //Puliamo griglia
        var tds = document.getElementsByTagName('td');
        for (var i = 0, td = tds.length; i < td; i++) {
            tds[i].style.backgroundColor = 'black';
        }

        //Se si è fatto un turno a testa cambiamo il gioco
        if (touchMinigame.turnNumber >= game.playerManager.players.length - 1) {
            touchMinigame.minigameInstructionText = game.gamePhaser.add.text(game.gamePhaser.world.centerX, game.gamePhaser.world.centerY, "Gioco finito! Cambio minigioco", game.textStyles.titleStyle);
            touchMinigame.minigameInstructionText.fontSize = 80;
            touchMinigame.minigameInstructionText.anchor.setTo(0.5, 0.5);

            //  Create our Timer
            var timer2 = game.gamePhaser.time.create(false);

            //  Set a TimerEvent to occur after 3 seconds
            timer2.add(3000, function () {
                game.minigameManager.loadSpecificMinigame("shakeMinigame");
            }, this);
            timer2.start();

        }
        //Altrimenti nuovo turno
        else {
            touchMinigame.minigameInstructionText = game.gamePhaser.add.text(game.gamePhaser.world.centerX, game.gamePhaser.world.centerY, "Turno finito! Cambio ruoli..", game.textStyles.titleStyle);
            touchMinigame.minigameInstructionText.fontSize = 80;
            touchMinigame.minigameInstructionText.anchor.setTo(0.5, 0.5);

            //  Create our Timer
            var timer2 = game.gamePhaser.time.create(false);

            //  Set a TimerEvent to occur after 3 seconds
            timer2.add(3000, function () {
                touchMinigame.minigameInstructionText.setText("3");
            }, this);
            timer2.add(4000, function () {
                touchMinigame.minigameInstructionText.setText("2");

            }, this);
            timer2.add(5000, function () {
                touchMinigame.minigameInstructionText.setText("1");
            }, this);
            timer2.add(6000, function () {
                touchMinigame.minigameInstructionText.setText("GO!");

                game.gamePhaser.add.tween(touchMinigame.minigameInstructionText).to({alpha: 0}, 250, "Linear", true);

                //Mandiamo all'artista
                var message = {turninc: 'increment'};
                game.gameManager.sendGameMessageToAllConnectedPlayers(message);

                //Riparte il timer
                this.initClock();

                //Griglia
                var wrapper = document.getElementById("wrapper");
                wrapper.style.visibility = "visible";

            }, this);
            //  Start the timer running - this is important!
            timer2.start();
        }

    },
    stopClock: function () {

        touchMinigame.clocktimer.stop(false);

        touchMinigame.clocktext.text = "Tempo fermato";

        console.log('stopClock - Tempo fermato');
    },
    initClock: function () {
        touchMinigame.clockseconds = 30 + 1; // set countdown seconds, +1 because initial display will also decrease with 1

        this.updateDisplay(); // initial display

        touchMinigame.clocktimer.start();
    },
    updateDisplay: function () {
        // count down seconds

        touchMinigame.clockseconds = touchMinigame.clockseconds - 1;

        //console.log('updateClock - seconds left: '+touchMinigame.clockseconds);


        // check if time is up

        if (touchMinigame.clockseconds <= 0) {

            // ohnoes!

            touchMinigame.stopClock();

            console.log('updateClock - Tempo scaduto');

            touchMinigame.clocktext.text = 'Tempo scaduto!';
            this.changeTurn();

        } else {

            // update display

            var minutes = Math.floor(touchMinigame.clockseconds / 60);

            var seconds = (touchMinigame.clockseconds - minutes * 60);

            touchMinigame.clocktext.text = "Tempo rimasto " + (("0" + minutes).substr(-2) + ":" + ("0" + seconds).substr(-2));

        }
        ;
    }

}