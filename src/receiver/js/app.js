/**
 * app.js // Main entry point
 */


var game = new Phaser.Game(window.innerWidth, window.innerHeight, Phaser.AUTO, '');
//Aggiungiamo i vari stati

game.state.add("boot",bootState);
game.state.add("titleScreen",titleScreenState);
game.state.add("lobby",lobbyState);

//Avviamo il gioco
game.state.start("boot");
