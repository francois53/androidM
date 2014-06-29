game.module(
    'game.assets'
)
.require(
    'engine.audio'
)
.body(function() {

// Sprites
game.addAsset('media/nyan.png');
game.addAsset('media/nyan2.png');
game.addAsset('media/logo2.png');
game.addAsset('media/logo1.png');
game.addAsset('media/cloud4.png');
game.addAsset('media/cloud3.png');
game.addAsset('media/cloud2.png');
game.addAsset('media/cloud1.png');
game.addAsset('media/ground.png');
game.addAsset('media/bushes.png');
game.addAsset('media/parallax3.png');
game.addAsset('media/parallax2.png');
game.addAsset('media/parallax1.png');
game.addAsset('media/particle.png');
game.addAsset('media/particle2.png');
game.addAsset('media/particle3.png');
game.addAsset('media/particle4.png');
game.addAsset('media/particle5.png');
game.addAsset('media/bar.png');
game.addAsset('media/gameover.png');
game.addAsset('media/new.png');
game.addAsset('media/restart.png');
game.addAsset('media/madewithpanda.png');

// Font
game.addAsset('media/font.fnt');

// Sounds
game.addAudio('media/sound/explosion.m4a', 'explosion');
game.addAudio('media/sound/jump.m4a', 'jump');
game.addAudio('media/sound/score.m4a', 'score');
game.addAudio('media/sound/highscore.m4a', 'highscore');
game.addAudio('media/sound/music.m4a', 'music');

});