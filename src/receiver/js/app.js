/**
 * app.js // Main entry point
 */




window.game = {};

function Player(id, name, avatar) {
    //General
    this.Id = id;
    this.Name = name;
    this.Avatar = avatar;

    //Sprite
    this.sprite = game.gamePhaser.add.sprite(0, 0, 'avatars', game.avatarsMap.get(avatar));
    this.sprite.anchor.set(0.5);
    this.sprite.scale.setTo(0.5, 0.5);
    this.sprite.visible = false;
    this.sprite.z = 0.8;

    //Shadow
    this.shadow = game.gamePhaser.add.sprite(0, 0, 'shadow');
    this.shadow.anchor.set(0.5);
    this.shadow.scale.setTo(0.2, 0.2);
    this.sprite.visible = false;
    this.shadow.z = 1;

    //Player name
    this.nameTitle = game.gamePhaser.add.text(0, 0, this.Name, game.textStyles.nameStyle);
    this.nameTitle.anchor.set(0.5);
    this.nameTitle.fontSize = 30;
};

function PlayerManager() {
    this.players = [];
    this.hostId="";
};

PlayerManager.prototype.setHost = function (id) {
    this.hostId = id;
    var message = { host: 'true' };
    game.gameManager.sendGameMessageToPlayer(id, message);
    console.log("Cambiato host in: " + id+" e inviato messaggio: "+message);
};


PlayerManager.prototype.isThereAHost = function (id) {
    if (this.hostId != "") return true;
    else return false;
};




PlayerManager.prototype.addPlayerToList = function (id, name, avatar) {
    this.players.push(new Player(id, name, avatar));
};

PlayerManager.prototype.removePlayerFromList = function (id) {
    for (var i = 0; i < this.players.length; ++i) {
        if (this.players[i].Id == id) {
            this.players[i].sprite.destroy();
            this.players[i].shadow.destroy();
            this.players[i].nameTitle.destroy();
            //Eliminiamo il giocatore dall'elenco e aggiorniamo la lista
            this.players.splice(i, 1);
            return
        }
    }
   // console.log("Player con questo id not found");
}

//Setta posizione e rende visibile il giocatore
PlayerManager.prototype.showPlayer = function (id, logicValue) {

//Cerchiamo il giocatore con quell'id
    for (var i = 0; i < this.players.length; ++i) {
        if (this.players[i].Id == id) {
            this.players[i].sprite.visible = true;
            game.gamePhaser.world.bringToTop(this.players[i].sprite);
            return;
        }
    }
    console.log("Player con questo id not found");
};


//Ritorna un oggetto {x,y} con la posizione dello sprite del giocatore
PlayerManager.prototype.getPlayerPosition = function (id) {
    for (var i = 0; i < this.players.length; ++i) {
        if (this.players[i].Id == id) {
            return {x: this.players[i].sprite.x, y: this.players[i].sprite.y};
        }
    }
    //Trovato nessun
    return null;
    console.log("Player con questo id not found");
}

//Imposta la posizione del giocatore
PlayerManager.prototype.setPlayerPosition = function (id, x, y) {
    for (var i = 0; i < this.players.length; ++i) {
        if (this.players[i].Id == id) {
            this.players[i].sprite.x = x;
            this.players[i].sprite.y = y;
            this.players[i].shadow.x = x;
            this.players[i].shadow.y = y + this.players[i].sprite.height / 2;
            return;
        }
    }
    console.log("Player con questo id not found");
}

//Imposta la posizione del nome (phaser)
PlayerManager.prototype.setNamePosition = function (id, x, y) {
    for (var i = 0; i < this.players.length; ++i) {
        if (this.players[i].Id == id) {
            this.players[i].nameTitle.x = x;
            this.players[i].nameTitle.y = y;
            return;
        }
    }
    console.log("Player con questo id not found");
}

//Rende visibile o meno l'ombra del giocatore
PlayerManager.prototype.showShadowUnderPlayer = function (id, logicValue) {
    for (var i = 0; i < this.players.length; ++i) {
        if (this.players[i].Id == id) {
            this.players[i].shadow.visible = logicValue;
            return;
        }
    }
    console.log("Player con questo id not found");
}

function TextStyles() {

    this.titleStyle = {
        font: "berlin",
        fill: "#ffcc5c",
        stroke: "black",
        strokeThickness: 5,
        boundsAlignH: "center",
        boundsAlignV: "middle"
    };

    this.normalStyle = {
        font: "berlin",
        fill: "#000000",
        boundsAlignH: "center",
        boundsAlignV: "middle"
    };

    this.nameStyle = {
        font: "berlin",
        fill: "#29abe2",
        stroke: "black",
        strokeThickness: 5,
        boundsAlignH: "center",
        boundsAlignV: "middle"
    }
}

function PartyCast(gameManager) {
    this.gameManager = gameManager;
    this.debugUi = new cast.receiver.games.debug.DebugUI(this.gameManager);

    this.debugUi.open();
    this.playerManager = new PlayerManager();

    this.avatarsMap = new Map();
    this.avatarsMap.set(1, "1.png");
    this.avatarsMap.set(2, "2.png");
    this.avatarsMap.set(3, "3.png");
    this.avatarsMap.set(4, "4.png");
    this.avatarsMap.set(5, "5.png");
    this.avatarsMap.set(6, "6.png");

    this.textStyles = new TextStyles();


    this.gamePhaser = new Phaser.Game(window.innerWidth, window.innerHeight, Phaser.AUTO, '');
    //Aggiungiamo i vari stati
    this.gamePhaser.state.add("boot", bootState);
    this.gamePhaser.state.add("titleScreen", titleScreenState);
    this.gamePhaser.state.add("lobby", lobbyState);

    //minigames
    this.gamePhaser.state.add("touchMinigame", touchMinigame);
    this.gamePhaser.state.add("micMinigame", micMinigame);
    this.gamePhaser.state.add("shakeMinigame", shakeMinigame);
    this.gamePhaser.state.add("gyroMinigame", gyroMinigame);


    this.minigameManager = new MinigameManager();

};

PartyCast.prototype.run = function () {
    this.gamePhaser.state.start("boot");
};


/**
 * Main entry point. This is not meant to be compiled so suppressing missing
 * goog.require checks.
 */
var initialize = function () {
    var castReceiverManager = cast.receiver.CastReceiverManager.getInstance();
    var appConfig = new cast.receiver.CastReceiverManager.Config();

    appConfig.statusText = 'PartyCast';
    // In production, use the default maxInactivity instead of using this.
    appConfig.maxInactivity = 6000;

    // Create the game before starting castReceiverManager to make sure any extra
    // cast namespaces can be set up.

    var gameConfig = new cast.receiver.games.GameManagerConfig();
    gameConfig.applicationName = 'PartyCast';
    gameConfig.maxPlayers = 4;


    var gameManager = new cast.receiver.games.GameManager(gameConfig);
    //Chiudiamo la lobby
    gameManager.updateLobbyState(
        cast.receiver.games.LobbyState.CLOSED, null);

    game = new PartyCast(gameManager);

    var startGame = function () {
        game.run(function () {
            console.log('Game running.');
            gameManager.updateGameStatusText('Game running.');
        });
    };

    castReceiverManager.onReady = function (event) {
        if (document.readyState === 'complete') {
            startGame();
        } else {
            window.onload = startGame;
        }
    };
    castReceiverManager.start(appConfig);
};

if (document.readyState === 'complete') {
    initialize();
} else {
    /** Main entry point. */
    window.onload = initialize;
}







