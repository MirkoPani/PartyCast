//lobby
var lobbyState = {
    playerCountTitle: {},
    create: function () {
        game.gamePhaser.stage.backgroundColor = "#96ceb4";

        //Settiamo la lobby open
        game.gameManager.updateLobbyState(
            cast.receiver.games.LobbyState.OPEN, null);

        //Scritta Lobby
        var lobbyTitle = game.gamePhaser.add.text(game.gamePhaser.world.centerX, 80, "Lobby", game.textStyles.titleStyle);
        lobbyTitle.anchor.setTo(0.5, 0.5);
        lobbyTitle.fontSize = 100;

        //Scritta: in attesa di altri giocatori
        this.playerCountTitle = game.gamePhaser.add.text(game.gamePhaser.world.centerX, 180, "Giocatori connessi: " + game.playerManager.players.length + "/4", game.textStyles.normalStyle);
        this.playerCountTitle.fontSize = 50;
        this.playerCountTitle.anchor.setTo(0.5, 0.5);

        //Player ha joinato
        game.gameManager.addEventListener(cast.receiver.games.EventType.PLAYER_READY,
            function (event) {
                console.log("Ricevuto playerReady. id:" + event.playerInfo.playerId);
                lobbyState.handlePlayerReady(event.playerInfo.playerId, event.requestExtraMessageData);
                lobbyState.updatePlayerCountTitle();

                //Se è il primo a connettersi o l'host non esiste lo settiamo
                if (game.playerManager.isThereAHost() == false) {
                    console.log("Non c'è un host. Settiamo host");
                    game.playerManager.setHost(event.playerInfo.playerId);
                }

            });
        //Player uscito involontariamente oppure ha chiuso l'app
        game.gameManager.addEventListener(cast.receiver.games.EventType.PLAYER_DROPPED,
            function (event) {
                console.log("Ricevuto playerDropped. id:" + event.playerInfo.playerId);
                lobbyState.handlePlayerDropped(event.playerInfo.playerId, event.requestExtraMessageData);
                lobbyState.updatePlayerCountTitle();
            });

        //Player uscito volontariamente
        game.gameManager.addEventListener(cast.receiver.games.EventType.PLAYER_QUIT,
            function (event) {
                console.log("Ricevuto playerQuit. id:" + event.playerInfo.playerId);
                lobbyState.handlePlayerDropped(event.playerInfo.playerId, event.requestExtraMessageData);
                lobbyState.updatePlayerCountTitle();
            });


        //L'host ha chiesto di iniziare il gioco
        game.gameManager.addEventListener(cast.receiver.games.EventType.PLAYER_PLAYING, lobbyState.handlePlayerPlaying);

    },
    handlePlayerPlaying: function (event) {
        console.log("Ricevuto Richiesta PLAYING. id:" + event.playerInfo.playerId);
        //Se non si è almeno in due non facciamo niente
        if (game.playerManager.players.length < 2) {
            console.log("Servono almeno due giocatori");
            game.gameManager.updatePlayerState(event.playerInfo.playerId,
                cast.receiver.games.PlayerState.READY, null);
        }
        else {// Update all ready players to playing state.
            var players = game.gameManager.getPlayers();

            game.gameManager.removeEventListener(cast.receiver.games.EventType.PLAYER_PLAYING, lobbyState.handlePlayerPlaying);

            for (var i = 0; i < players.length; i++) {
                if (players[i].playerState == cast.receiver.games.PlayerState.READY) {
                    game.gameManager.updatePlayerState(players[i].playerId,
                        cast.receiver.games.PlayerState.PLAYING, null);
                    console.log("Ho appena aggiornato playing un utente");
                }
            }

            //Settiamo la lobby close
            game.gameManager.updateLobbyState(
                cast.receiver.games.LobbyState.CLOSED, null);

            lobbyState.startMinigames();
            console.log("Ho appena chiamato startMinigame, sono in playing");
        }
    },

    handlePlayerReady: function (playerId, playerData) {

        if (game.playerManager.isPlayerAlreadyConnected(playerId) == false) {
            game.gameManager.updatePlayerData(playerId, playerData);
            game.playerManager.addPlayerToList(playerId, playerData.Name, playerData.Avatar);
            game.playerManager.setPlayerPosition(playerId, (window.innerWidth / 5) * game.playerManager.players.length + 1, 400);
            game.playerManager.setNamePosition(playerId, (window.innerWidth / 5) * game.playerManager.players.length + 1, 550);
            game.playerManager.showPlayer(playerId, true);
            game.playerManager.showShadowUnderPlayer(playerId, true);
        }
    },
    handlePlayerDropped: function (playerId, playerData) {

        game.playerManager.removePlayerFromList(playerId);

        //se e' uscito host ne facciamo uno nuovo
        if (game.playerManager.hostId == playerId) {
            //se c'è qualcun altro diventa lui host
            if (game.playerManager.players.length > 0) {
                console.log("L'host è andato via e ora lo cambiamo. Nuovo host: " + game.playerManager.players[0].Id);
                game.playerManager.setHost(game.playerManager.players[0].Id);
            }
            //Altrimenti non c'è e aspettiamo
            else {
                game.playerManager.hostId = "";

            }
        }


        //Riposizioniamo tutti gli altri giocatori
        for (var i = 0; i < game.playerManager.players.length; i++) {
            var id = game.playerManager.players[i].Id;
            game.playerManager.setPlayerPosition(id, (window.innerWidth / 5) * (i + 1), 400);
            game.playerManager.setNamePosition(id, (window.innerWidth / 5) * (i + 1), 550);
        }

    },
    updatePlayerCountTitle: function () {
        this.playerCountTitle.setText("Giocatori connessi: " + game.playerManager.players.length + "/4");
    },
    startMinigames: function () {
        console.log("Startminigames");
        //Settiamo il gioco come running
        game.gameManager.updateGameplayState(
            cast.receiver.games.GameplayState.LOADING, false);

        game.minigameManager.loadSpecificMinigame("shakeMinigame");
        // game.minigameManager.loadRandomMinigame();
    },
    shutdown: function () {
        console.log("Lobby-shutdown");
        for (var i = 0; i < game.playerManager.players.length; i++) {
            game.gamePhaser.world.remove(game.playerManager.players[i].sprite);
            game.gamePhaser.world.remove(game.playerManager.players[i].shadow);
            game.gamePhaser.world.remove(game.playerManager.players[i].nameTitle);
        }
        //Settiamo la lobby chiusa
        game.gameManager.updateLobbyState(
            cast.receiver.games.LobbyState.CLOSED, null);

    }


}