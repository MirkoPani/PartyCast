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
                lobbyState.handlePlayerQuit(event.playerInfo.playerId, event.requestExtraMessageData);
                lobbyState.updatePlayerCountTitle();
            });

        //L'host ha chiesto di iniziare il gioco
        game.gameManager.addEventListener(cast.receiver.games.EventType.PLAYER_PLAYING,
            function (event) {
                console.log("Ricevuto Richiesta PLAYING. id:" + event.playerInfo.playerId);

                // Update all ready players to playing state.
                var players = game.gameManager.getPlayers();
                for (var i = 0; i < players.length; i++) {
                    if (players[i].playerState == cast.receiver.games.PlayerState.READY) {
                        game.gameManager.updatePlayerState(players[i].playerId,
                            cast.receiver.games.PlayerState.PLAYING, null);
                    }
                }

                //Settiamo la lobby close
                game.gameManager.updateLobbyState(
                    cast.receiver.games.LobbyState.CLOSED, null);

                //Settiamo il gioco come running
                game.gameManager.updateGameplayState(
                    cast.receiver.games.GameplayState.RUNNING, null);

                lobbyState.startMinigames();
            });

    },
    handlePlayerReady: function (playerId, playerData) {
        game.gameManager.updatePlayerData(playerId, playerData);
        game.playerManager.addPlayerToList(playerId, playerData.Name, playerData.Avatar);
        game.playerManager.setPlayerPosition(playerId, (window.innerWidth / 5) * game.playerManager.players.length + 1, 400);
        game.playerManager.setNamePosition(playerId, (window.innerWidth / 5) * game.playerManager.players.length + 1, 550);
        game.playerManager.showPlayer(playerId, true);
        game.playerManager.showShadowUnderPlayer(playerId, true);
    },
    handlePlayerDropped: function (playerId, playerData) {

        game.playerManager.removePlayerFromList(playerId);
        //Riposizioniamo tutti gli altri giocatori
        for (var i = 0; i < game.playerManager.players.length; i++) {
            var id = game.playerManager.players[i].Id;
            game.playerManager.setPlayerPosition(id, (window.innerWidth / 5) * (i + 1), 400);
            game.playerManager.setNamePosition(id, (window.innerWidth / 5) * (i + 1), 550);
        }

    },
    handlePlayerQuit: function (playerId, playerData) {

        game.playerManager.removePlayerFromList(playerId);
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
        game.minigameManager.loadMinigame("touchMinigame");
    },
    shutdown: function () {
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