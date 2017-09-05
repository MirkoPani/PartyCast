//endGame
var endGame = {
    endText: {},
    winnerText: {},
    nameofWinnerText: {},
    spriteOfWinner: {},
    playersSorted:[],
    preload: function () {

    },
    create: function () {
        game.gamePhaser.stage.backgroundColor = "#ffcc5c";
        endGame.endText = game.gamePhaser.add.text(game.gamePhaser.world.centerX, 50, "Classifica", game.textStyles.nameStyle);
        endGame.endText.fontSize = 60;
        endGame.endText.anchor.setTo(0.5, 0.5);

        //Lista di giocatori ordinata per punteggio
        endGame.playersSorted = game.playerManager.players;
        endGame.playersSorted.sort(function (a, b) {
            return b.points - a.points;
        });

        //Abbiamo solo un vincitore, lo mostriamo in grande
        if (endGame.playersSorted[0].points != endGame.playersSorted[1].points) {
            endGame.winnerText = game.gamePhaser.add.text(window.innerWidth / 6, window.innerHeight / 4, "Vincitore - " + endGame.playersSorted[0].Name, game.textStyles.nameStyle);
            endGame.winnerText.fontSize = 40;

            endGame.spriteOfWinner = game.gamePhaser.add.sprite((window.innerWidth / 6), window.innerHeight / 4 + 100, 'avatars', game.avatarsMap.get(endGame.playersSorted[0].Avatar));
            endGame.spriteOfWinner.visible = true;
            endGame.spriteOfWinner.z = 0.8;
        }
        //Piu persone hanno preso lo stesso punteggio..
        else {
            var winnerString = "Vincitori a pari merito:\n";

            for (var i = 0; i < playersSorted.length; i++) {
                if (endGame.playersSorted[0].points == endGame.playersSorted[i].points) {
                    winnerString += endGame.playersSorted[i].Name + "\n";
                }

            }
            endGame.winnerText = game.gamePhaser.add.text(window.innerWidth / 6, window.innerHeight / 4, winnerString, game.textStyles.nameStyle);
            endGame.winnerText.fontSize = 40;
        }
        /////////////////////////////////////////////////////////////////////////////////

        var altezzaTab = window.innerHeight - (window.innerHeight * .3);
        var larghezzaTab = window.innerWidth - (window.innerWidth * .6);
        var posXTab = game.gamePhaser.world.centerX;
        var posYTab = window.innerHeight / 4;

        var riservato = 30;
        var fattore = (altezzaTab - riservato) / game.playerManager.players.length;
        var inizioLinea = {X: posXTab + 50, Y: posYTab + riservato};
        var fineLinea = {X: posXTab + larghezzaTab - 50, Y: posYTab + riservato};

        var lunghezzaLinea = fineLinea.Y - inizioLinea.Y;
        var centroLinea = posXTab + (larghezzaTab / 2);
        var trequarti = centroLinea + (larghezzaTab / 4);

        this.pointsTable = game.gamePhaser.add.graphics(0, 0);
        this.pointsTable.lineStyle(1);

        //rettangolo
        this.pointsTable.beginFill(0x333333, 0.5);
        this.pointsTable.drawRect(posXTab, posYTab, larghezzaTab, altezzaTab);
        this.pointsTable.endFill();


        var pointsArrayText = []

        console.log("fattore: " + fattore);
        console.log("posY: " + posYTab);
        console.log("Altezza: " + altezzaTab);
        console.log("posX: " + posXTab);
        for (var i = 0; i < game.playerManager.players.length; i++) {
            var posy = posYTab + riservato + fattore * (i + 1) - (game.playerManager.players[i].sprite.height / game.playerManager.players.length);
            console.log("posy: " + posy);

            game.gamePhaser.add.existing(game.playerManager.players[i].sprite);
            game.playerManager.players[i].sprite.scale.setTo(0.25, 0.25);
            game.playerManager.players[i].sprite.anchor.setTo(0.5, 0.5);
            game.gamePhaser.add.existing(game.playerManager.players[i].nameTitle);
            game.playerManager.players[i].nameTitle.anchor.setTo(0, 1);

            game.playerManager.setPlayerPosition(game.playerManager.players[i].Id, posXTab + 50, posy - 20);
            game.playerManager.setNamePosition(game.playerManager.players[i].Id, posXTab + 100 + game.playerManager.players[i].sprite.width, posy);
            game.playerManager.showPlayer(game.playerManager.players[i].Id, true);

            //Mostriamo punti
            pointsArrayText.push(game.gamePhaser.add.text(trequarti + 50, posy, game.playerManager.players[i].points, game.textStyles.titleStyle));
            pointsArrayText[i].fontSize = 25;
            pointsArrayText[i].anchor.setTo(0, 1);
        }

        endGame.sendPointsMessage();

    },
    shutdown: function () {

    },
    sendPointsMessage:function () {
        var message={classifica:[
            
        ]};

        for(var i=0;i<endGame.playersSorted.length;i++)
        {
            message.classifica.push({name:endGame.playersSorted[i].Name,
                points:endGame.playersSorted[i].points,
                avatar:endGame.playersSorted[i].Avatar});
        }
        game.gameManager.sendGameMessageToAllConnectedPlayers(message);
        console.log("Inviato messaggio classifica.");
        console.log(message);
    }
};