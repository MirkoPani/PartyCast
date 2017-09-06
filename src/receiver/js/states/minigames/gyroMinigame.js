//gyroMinigame
var gyroMinigame = {
    ball: {},
    keys: {},
    velocity: 8,
    left: false,
    right: false,
    up: false,
    down: false,
    preload: function () {
        game.gamePhaser.load.image('ball', 'img/atari130xe.png');
        //game.gamePhaser.load.spritesheet('bullets', 'img/balls.png', 17, 17);
    },
    create: function () {
        game.gamePhaser.stage.backgroundColor = "#63ce7a";
        game.minigameManager.drawHud();
        //TODO SHOW INSTRUCTIONS
        //TODO POINTS TABLE
        //TODO GESTIONE SINGOLA MACCHININA
        //TODO GENERAZIONE AREE IN CUI PASSARE SOPRA
        //TODO EVENTUALE COLLISIONE

        // Add the ball to the middle of the game area
        gyroMinigame.ball = game.gamePhaser.add.sprite(game.gamePhaser.world.centerX, game.gamePhaser.world.centerY, 'ball');
        gyroMinigame.ball.anchor.set(0.5, 0.5);

        // Add key input to the game
        gyroMinigame.keys = game.gamePhaser.input.keyboard.createCursorKeys();


        game.gameManager.addEventListener(
            cast.receiver.games.EventType.GAME_MESSAGE_RECEIVED, gyroMinigame.checkMessage);


    },
    update: function () {
        // console.log("update");

        if (gyroMinigame.left) {
            gyroMinigame.ball.x -= gyroMinigame.velocity;
            //gyroMinigame.left = false;
        }
        if (gyroMinigame.right) {
            gyroMinigame.ball.x += gyroMinigame.velocity;
            //gyroMinigame.right = false;
        }
        if (gyroMinigame.up) {
            gyroMinigame.ball.y -= gyroMinigame.velocity;
            //gyroMinigame.up = false;
        }
        if (gyroMinigame.down) {
            gyroMinigame.ball.y += gyroMinigame.velocity;
            //gyroMinigame.down = false;
        }

        //Reset posizioni


        // Prevent ball from escaping outside the stage's boundaries
        var halfWidth = gyroMinigame.ball.width / 2;
        var halfHeight = gyroMinigame.ball.height / 2;
        if ((gyroMinigame.ball.x - halfWidth) < 0) {
            gyroMinigame.ball.x = halfWidth;
        }
        if ((gyroMinigame.ball.x + halfWidth) > game.gamePhaser.width) {
            gyroMinigame.ball.x = game.gamePhaser.width - halfWidth;
        }
        if ((gyroMinigame.ball.y - halfHeight) < 0) {
            gyroMinigame.ball.y = halfHeight;
        }
        if ((gyroMinigame.ball.y + halfHeight) > game.gamePhaser.height) {
            gyroMinigame.ball.y = game.gamePhaser.height - halfHeight;
        }

    },
    checkMessage: function (event) {
        // console.log("checkmessage chiamato message= " + event.requestExtraMessageData);
        var message = event.requestExtraMessageData;

        if (message.hasOwnProperty("movUpDown")) {
            switch (message.movUpDown) {
                case 'Up':
                    gyroMinigame.up = true;
                    gyroMinigame.down=false;
                    console.log("up true");
                    break;
                case 'Down':
                    gyroMinigame.down = true;
                    console.log("down true");
                    gyroMinigame.up=false;
                    break;
                default:    gyroMinigame.up=false;
                            gyroMinigame.down=false;
                    break;
            }

        }

        if (message.hasOwnProperty("movLeftRight")) {
            switch (message.movLeftRight) {
                case 'Left':
                    gyroMinigame.left = true;
                    gyroMinigame.right=false;
                    console.log("left true");
                    break;
                case 'Right':
                    gyroMinigame.right = true;
                    gyroMinigame.left=false;
                    console.log("right true");
                    break;
                default:
                    gyroMinigame.right=false;
                    gyroMinigame.left=false;
                    break;
            }

        }
      //  gyroMinigame.update1();

    },
    shutdown:function(){
        game.gameManager.removeEventListener(
            cast.receiver.games.EventType.GAME_MESSAGE_RECEIVED, gyroMinigame.checkMessage);

    }

}