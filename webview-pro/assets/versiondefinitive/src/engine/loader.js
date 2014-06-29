/**    
    @module loader
    @namespace game
**/
game.module(
    'engine.loader'
)
.body(function(){ 'use strict';

/**
    Dynamic loader for assets and audio.
    @class Loader
    @extends game.Class
**/
game.Loader = game.Class.extend({
    /**
        Scene to start, when loader is finished.
        @property {game.Scene} scene
    **/
    scene: null,
    /**
        Number of files loaded.
        @property {Number} loaded
    **/
    loaded: 0,
    /**
        Percent of files loaded.
        @property {Number} percent
    **/
    percent: 0,
    /**
        Background color of preloader.
        @property {Number} backgroundColor
        @default 0x000000
    **/
    backgroundColor: 0x000000,
    /**
        List of assets to load.
        @property {Array} assets
    **/
    assets: [],
    /**
        List of sounds to load.
        @property {Array} assets
    **/
    sounds: [],
    
    init: function(scene) {
        var i;

        this.scene = scene || SceneGame;
        this.stage = game.system.stage;

        for (i = 0; i < game.resources.length; i++) {
            if(game.TextureCache[game.resources[i]]) continue;
            this.assets.push(game.Loader.getPath(game.resources[i]));
        }

        for(var name in game.Audio.queue) {
            this.sounds.push(name);
        }

        if(this.assets.length > 0) {
            this.loader = new game.AssetLoader(this.assets, true);
            this.loader.onProgress = this.progress.bind(this);
            this.loader.onComplete = this.loadAudio.bind(this);
            this.loader.onError = this.error.bind(this);
        }

        if(this.assets.length === 0 && this.sounds.length === 0) this.percent = 100;
    },

    initStage: function() {
        this.symbol = new game.Sprite(game.Texture.fromImage(game.Loader.logo));
        this.symbol.anchor.set(0.5, 1.0);
        this.symbol.position.set(game.system.width / 2, game.system.height / 2 + this.symbol.height / 2);
        this.stage.addChild(this.symbol);

        var barBg = new game.Graphics();
        barBg.beginFill(game.Loader.barBg);
        barBg.drawRect(0, 0, 200, 20);
        barBg.position.set(game.system.width / 2 - 100, game.system.height / 2 + this.symbol.height / 2 + 20);
        this.stage.addChild(barBg);

        this.bar = new game.Graphics();
        this.bar.beginFill(game.Loader.barColor);
        this.bar.drawRect(0, 0, 200, 20);
        this.bar.position.set(game.system.width / 2 - 100, game.system.height / 2 + this.symbol.height / 2 + 20);
        this.bar.scale.x = this.percent / 100;
        this.stage.addChild(this.bar);

        if(game.Tween && game.Loader.logoTween) {
            this.symbol.rotation = -0.1;

            var tween = new game.Tween(this.symbol)
                .to({rotation: 0.1}, 500)
                .easing(game.Tween.Easing.Cubic.InOut)
                .repeat()
                .yoyo();
            tween.start();
        }
    },

    /**
        Start loader.
        @method start
    **/
    start: function() {
        if(game.scene) {
            for (var i = this.stage.children.length - 1; i >= 0; i--) {
                this.stage.removeChild(this.stage.children[i]);
            }
            this.stage.setBackgroundColor(this.backgroundColor);

            this.stage.interactive = false; // this is not working, bug?

            this.stage.mousemove = this.stage.touchmove = null;
            this.stage.click = this.stage.tap = null;
            this.stage.mousedown = this.stage.touchstart = null;
            this.stage.mouseup = this.stage.mouseupoutside = this.stage.touchend = this.stage.touchendoutside = null;
            this.stage.mouseout = null;
        }
        if(game.audio) game.audio.stopAll();

        if(typeof(this.backgroundColor) === 'number') {
            var bg = new game.Graphics();
            bg.beginFill(this.backgroundColor);
            bg.drawRect(0, 0, game.system.width, game.system.height);
            this.stage.addChild(bg);
        }
        
        this.initStage();

        if(this.assets.length > 0) this.loader.load();
        else this.loadAudio();
        
        if(!game.scene) this.loopId = game.setGameLoop(this.run.bind(this), game.system.canvas);
        else game.scene = this;
    },

    /**
        Error loading file.
        @method error
        @param {String} msg
    **/
    error: function(msg) {
        if(msg) throw msg;
    },

    /**
        File loaded.
        @method progress
    **/
    progress: function(loader) {
        if(loader && loader.json && !loader.json.frames && !loader.json.bones) game.json[loader.url] = loader.json;
        this.loaded++;
        this.percent = Math.round(this.loaded / (this.assets.length + this.sounds.length) * 100);
        this.onPercentChange();
    },

    /**
        Called when percent is changed.
        @method onPercentChange
    **/
    onPercentChange: function() {
        if(this.bar) this.bar.scale.x = this.percent / 100;
    },

    /**
        Start loading audio.
        @method loadAudio
    **/
    loadAudio: function() {
        for (var i = this.sounds.length - 1; i >= 0; i--) {
            game.audio.load(this.sounds[i], this.progress.bind(this));
        }
    },

    /**
        All files loaded.
        @method ready
    **/
    ready: function() {
        this.setScene();
    },

    /**
        Set scene.
        @method setScene
    **/
    setScene: function() {
        if(game.system.retina || game.system.hires) {
            for(var i in game.TextureCache) {
                if(i.indexOf('@2x') !== -1) {
                    game.TextureCache[i.replace('@2x', '')] = game.TextureCache[i];
                    delete game.TextureCache[i];
                }
            }
        }
        game.resources.length = 0;
        game.Audio.resources = {};
        game.Timer.time = Number.MIN_VALUE;
        game.clearGameLoop(this.loopId);
        game.system.setScene(this.scene);
    },

    run: function() {
        this.last = game.Timer.time;
        game.Timer.update();
        game.system.delta = (game.Timer.time - this.last) / 1000;

        this.update();
        this.render();
    },

    update: function() {
        if(!this.startTime) this.startTime = Date.now();
        if(game.tweenEngine) game.tweenEngine.update();

        if(this._ready) return;
        if(this.timer) {
            if(this.timer.time() >= 0) {
                this._ready = true;
                this.ready();
            }
        } else if(this.loaded === this.assets.length + this.sounds.length) {
            // Everything loaded
            var loadTime = Date.now() - this.startTime;
            var waitTime = Math.max(100, game.Loader.timeout - loadTime);
            this.timer = new game.Timer(waitTime);
        }
    },

    render: function() {
        game.system.renderer.render(this.stage);
    }
});

/**
    Used to load correct file when in Retina/HiRes mode.
    @attribute {Function} getPath
    @param {String} path
**/
game.Loader.getPath = function(path) {
    return game.system.retina || game.system.hires ? path.replace(/\.(?=[^.]*$)/, '@2x.') : path;
};

/**
    Minimum time to show preloader, in milliseconds.
    @attribute {Number} timeout
    @default 500
**/
game.Loader.timeout = 500;

/**
    Background color of the loading bar.
    @attribute {Number} barBg
    @default 0x231f20
**/
game.Loader.barBg = 0x231f20;

/**
    Color of the loading bar.
    @attribute {Number} barColor
    @default 0xe6e7e8
**/
game.Loader.barColor = 0xe6e7e8;

/**
    Use tween on loader logo.
    @attribute {Boolean} tween
    @default true
**/
game.Loader.logoTween = true;

game.Loader.logo = 'data:image/gif;base64,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';

});