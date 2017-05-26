//titleScreen
var titleScreenState = {

    preload: function () {
        game.gamePhaser.load.image("logo", "img/logo.png");
        game.gamePhaser.load.atlasJSONHash('avatars', 'img/avatarsSheet.png', 'img/avatarsSheet.json');
        game.gamePhaser.load.image("shadow", "img/shadow.png");
    },

    create: function () {
        game.gamePhaser.stage.backgroundColor = "#96ceb4";

        //Visualizziamo logo
        var logoSprite = game.gamePhaser.add.image(game.gamePhaser.world.centerX, game.gamePhaser.world.centerY, 'logo');
        logoSprite.anchor.set(0.5);
        logoSprite.scale.setTo(0.5, 0.5);

        setTimeout(function () {
            game.gamePhaser.state.start("lobby");
        }, 8000);


    },

    start: function () {
        game.gamePhaser.state.start("lobby");
    },

    update: function () {

    }
}