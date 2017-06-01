//titleScreen
var titleScreenState = {

    preload: function () {
        game.gamePhaser.load.audio('wario', ['assets/wario.mp3']);
        game.gamePhaser.load.image("logo", "img/logo.png");
        game.gamePhaser.load.atlasJSONHash('avatars', 'img/avatarsSheet.png', 'img/avatarsSheet.json');
        game.gamePhaser.load.image("shadow", "img/shadow.png");
    },

    create: function () {
        game.music = game.gamePhaser.add.audio('wario');




        game.gamePhaser.stage.backgroundColor = "#96ceb4";

        //Visualizziamo logo
        var logoSprite = game.gamePhaser.add.image(game.gamePhaser.world.centerX, game.gamePhaser.world.centerY, 'logo');
        logoSprite.anchor.set(0.5);
        logoSprite.scale.setTo(0.5, 0.5);

       /* setTimeout(function () {
            game.gamePhaser.state.start("lobby");
         }, 2000);*/

        game.music.onDecoded.add(titleScreenState.loadComplete, this);
    },

    start: function () {
        game.gamePhaser.state.start("lobby");
    },

    loadComplete: function () {
        game.music.play();
        game.gamePhaser.state.start("lobby");
    }
}