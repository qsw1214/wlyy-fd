!function() {
    var __modules__ = {};
    function require(id) {
        var mod = __modules__[id];
        var exports = "exports";
        if (typeof mod === "object") {
            return mod;
        }
        if (!mod[exports]) {
            mod[exports] = {};
            mod[exports] = mod.call(mod[exports], require, mod[exports], mod) || mod[exports];
        }
        return mod[exports];
    }
    function define(path, fn) {
        __modules__[path] = fn;
    }
    define("jquery", function() {
        return jQuery;
    });
    define("popup", function(require) {
        var $ = require("jquery");
        var _count = 0;
        var _isIE6 = !("minWidth" in $("html")[0].style);
        var _isFixed = !_isIE6;
        function Popup() {
            this.destroyed = false;
            this.__popup = $("<div />").css({
                display:"none",
                position:"absolute",
                outline:0
            }).attr("tabindex", "-1").html(this.innerHTML).appendTo("body");
            this.__backdrop = this.__mask = $("<div />").css({
                opacity:.7,
                background:"#000"
            });
            this.node = this.__popup[0];
            this.backdrop = this.__backdrop[0];
            _count++;
        }
        $.extend(Popup.prototype, {
            node:null,
            backdrop:null,
            fixed:true,
            destroyed:true,
            open:false,
            returnValue:"",
            autofocus:true,
            align:"bottom left",
            innerHTML:"",
            className:"ui-popup",
            show:function(anchor) {
                if (this.destroyed) {
                    return this;
                }
                var that = this;
                var popup = this.__popup;
                var backdrop = this.__backdrop;
                this.__activeElement = this.__getActive();
                this.open = true;
                this.follow = anchor || this.follow;
                if (!this.__ready) {
                    popup.addClass(this.className).attr("role", this.modal ? "alertdialog" :"dialog").css("position", this.fixed ? "fixed" :"absolute");
                    if (!_isIE6) {
                        $(window).on("resize", $.proxy(this.reset, this));
                    }
                    if (this.modal) {
                        var backdropCss = {
                            position:"fixed",
                            left:0,
                            top:0,
                            width:"100%",
                            height:"100%",
                            overflow:"hidden",
                            userSelect:"none",
                            zIndex:this.zIndex || Popup.zIndex
                        };
                        popup.addClass(this.className + "-modal");
                        if (!_isFixed) {
                            $.extend(backdropCss, {
                                position:"absolute",
                                width:$(window).width() + "px",
                                height:$(document).height() + "px"
                            });
                        }
                        backdrop.css(backdropCss).attr({
                            tabindex:"0"
                        }).on("focus", $.proxy(this.focus, this));
                        this.__mask = backdrop.clone(true).attr("style", "").insertAfter(popup);
                        backdrop.addClass(this.className + "-backdrop").insertBefore(popup);
                        this.__ready = true;
                    }
                    if (!popup.html()) {
                        popup.html(this.innerHTML);
                    }
                }
                popup.addClass(this.className + "-show").show();
                backdrop.show();
                this.reset().focus();
                this.__dispatchEvent("show");
                return this;
            },
            showModal:function() {
                this.modal = true;
                return this.show.apply(this, arguments);
            },
            close:function(result) {
                if (!this.destroyed && this.open) {
                    if (result !== undefined) {
                        this.returnValue = result;
                    }
                    this.__popup.hide().removeClass(this.className + "-show");
                    this.__backdrop.hide();
                    this.open = false;
                    this.blur();
                    this.__dispatchEvent("close");
                }
                return this;
            },
            remove:function() {
                if (this.destroyed) {
                    return this;
                }
                this.__dispatchEvent("beforeremove");
                if (Popup.current === this) {
                    Popup.current = null;
                }
                this.__popup.remove();
                this.__backdrop.remove();
                this.__mask.remove();
                if (!_isIE6) {
                    $(window).off("resize", this.reset);
                }
                this.__dispatchEvent("remove");
                for (var i in this) {
                    delete this[i];
                }
                return this;
            },
            reset:function() {
                var elem = this.follow;
                var bottom = this.options.bottom;
                if (elem) {
                    this.__follow(elem);
                } else if (bottom) {
                    this.__bottom();
                } else {
                    this.__center();
                }
                this.__dispatchEvent("reset");
                return this;
            },
            focus:function() {
                var node = this.node;
                var popup = this.__popup;
                var current = Popup.current;
                var index = this.zIndex = Popup.zIndex++;
                if (current && current !== this) {
                    current.blur(false);
                }
                if (!$.contains(node, this.__getActive())) {
                    var autofocus = popup.find("[autofocus]")[0];
                    if (!this._autofocus && autofocus) {
                        this._autofocus = true;
                    } else {
                        autofocus = node;
                    }
                    this.__focus(autofocus);
                }
                popup.css("zIndex", index);
                Popup.current = this;
                popup.addClass(this.className + "-focus");
                this.__dispatchEvent("focus");
                return this;
            },
            blur:function() {
                var activeElement = this.__activeElement;
                var isBlur = arguments[0];
                if (isBlur !== false) {
                    this.__focus(activeElement);
                }
                this._autofocus = false;
                this.__popup.removeClass(this.className + "-focus");
                this.__dispatchEvent("blur");
                return this;
            },
            addEventListener:function(type, callback) {
                this.__getEventListener(type).push(callback);
                return this;
            },
            removeEventListener:function(type, callback) {
                var listeners = this.__getEventListener(type);
                for (var i = 0; i < listeners.length; i++) {
                    if (callback === listeners[i]) {
                        listeners.splice(i--, 1);
                    }
                }
                return this;
            },
            __getEventListener:function(type) {
                var listener = this.__listener;
                if (!listener) {
                    listener = this.__listener = {};
                }
                if (!listener[type]) {
                    listener[type] = [];
                }
                return listener[type];
            },
            __dispatchEvent:function(type) {
                var listeners = this.__getEventListener(type);
                if (this["on" + type]) {
                    this["on" + type]();
                }
                for (var i = 0; i < listeners.length; i++) {
                    listeners[i].call(this);
                }
            },
            __focus:function(elem) {
                try {
                    if (this.autofocus && !/^iframe$/i.test(elem.nodeName)) {
                        elem.focus();
                    }
                } catch (e) {}
            },
            __getActive:function() {
                try {
                    var activeElement = document.activeElement;
                    var contentDocument = activeElement.contentDocument;
                    var elem = contentDocument && contentDocument.activeElement || activeElement;
                    return elem;
                } catch (e) {}
            },
            __center:function() {
                var popup = this.__popup;
                var $window = $(window);
                var $document = $(document);
                var fixed = this.fixed;
                var dl = fixed ? 0 :$document.scrollLeft();
                var dt = fixed ? 0 :$document.scrollTop();
                var ww = $window.width();
                var wh = $window.height();
                var ow = popup.width();
                var oh = popup.height();
                var left = (ww - ow) / 2 + dl;
                var top = (wh - oh) * 382 / 1e3 + dt;
                var style = popup[0].style;
                style.left = Math.max(parseInt(left), dl) + "px";
                style.top = Math.max(parseInt(top), dt) + "px";
                if (this.__browser.versions.ios) {
                    style.top = "50%";
                    style.position = "fixed";
                    style.marginTop = "-" + popup.height() / 2 + "px";
                }
            },
			 __bottom:function() {
                var popup = this.__popup;
                var $window = $(window);
                var $document = $(document);
                var fixed = this.fixed;
                var dl = fixed ? 0 :$document.scrollLeft();
                var dt = fixed ? 0 :$document.scrollTop();
                var ww = $window.width();
                var wh = $window.height();
                var ow = popup.width();
                var oh = popup.height();
                var left = (ww - ow) / 2 + dl;
                var top = (wh - oh) * 382 / 1e3 + dt;
                var style = popup[0].style;
                style.left = Math.max(parseInt(left), dl) + "px";
                style.bottom = "50px";
                if (this.__browser.versions.ios) {
                    style.bottom = "50px";
                    style.position = "fixed";
                    style.marginTop = "-" + popup.height() / 2 + "px";
                }
            },
            __browser:{
                versions:function() {
                    var u = navigator.userAgent, app = navigator.appVersion;
                    return {
                        trident:u.indexOf("Trident") > -1,
                        presto:u.indexOf("Presto") > -1,
                        webKit:u.indexOf("AppleWebKit") > -1,
                        gecko:u.indexOf("Gecko") > -1 && u.indexOf("KHTML") == -1,
                        mobile:!!u.match(/AppleWebKit.*Mobile.*/),
                        ios:!!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/),
                        android:u.indexOf("Android") > -1 || u.indexOf("Linux") > -1,
                        iPhone:u.indexOf("iPhone") > -1,
                        iPad:u.indexOf("iPad") > -1,
                        webApp:u.indexOf("Safari") == -1
                    };
                }(),
                language:(navigator.browserLanguage || navigator.language).toLowerCase()
            },
            __follow:function(anchor) {
                var $elem = anchor.parentNode && $(anchor);
                var popup = this.__popup;
                if (this.__followSkin) {
                    popup.removeClass(this.__followSkin);
                }
                if ($elem) {
                    var o = $elem.offset();
                    if (o.left * o.top < 0) {
                        return this.__center();
                    }
                }
                var that = this;
                var fixed = this.fixed;
                var $window = $(window);
                var $document = $(document);
                var winWidth = $window.width();
                var winHeight = $window.height();
                var docLeft = $document.scrollLeft();
                var docTop = $document.scrollTop();
                var popupWidth = popup.width();
                var popupHeight = popup.height();
                var width = $elem ? $elem.outerWidth() :0;
                var height = $elem ? $elem.outerHeight() :0;
                var offset = this.__offset(anchor);
                var x = offset.left;
                var y = offset.top;
                var left = fixed ? x - docLeft :x;
                var top = fixed ? y - docTop :y;
                var minLeft = fixed ? 0 :docLeft;
                var minTop = fixed ? 0 :docTop;
                var maxLeft = minLeft + winWidth - popupWidth;
                var maxTop = minTop + winHeight - popupHeight;
                var css = {};
                var align = this.align.split(" ");
                var className = this.className + "-";
                var reverse = {
                    top:"bottom",
                    bottom:"top",
                    left:"right",
                    right:"left"
                };
                var name = {
                    top:"top",
                    bottom:"top",
                    left:"left",
                    right:"left"
                };
                var temp = [ {
                    top:top - popupHeight,
                    bottom:top + height,
                    left:left - popupWidth,
                    right:left + width
                }, {
                    top:top,
                    bottom:top - popupHeight + height,
                    left:left,
                    right:left - popupWidth + width
                } ];
                var center = {
                    left:left + width / 2 - popupWidth / 2,
                    top:top + height / 2 - popupHeight / 2
                };
                var range = {
                    left:[ minLeft, maxLeft ],
                    top:[ minTop, maxTop ]
                };
                $.each(align, function(i, val) {
                    if (temp[i][val] > range[name[val]][1]) {
                        val = align[i] = reverse[val];
                    }
                    if (temp[i][val] < range[name[val]][0]) {
                        align[i] = reverse[val];
                    }
                });
                if (!align[1]) {
                    name[align[1]] = name[align[0]] === "left" ? "top" :"left";
                    temp[1][align[1]] = center[name[align[1]]];
                }
                className += align.join("-") + " " + this.className + "-follow";
                that.__followSkin = className;
                if ($elem) {
                    popup.addClass(className);
                }
                css[name[align[0]]] = parseInt(temp[0][align[0]]);
                css[name[align[1]]] = parseInt(temp[1][align[1]]);
                popup.css(css);
            },
            __offset:function(anchor) {
                var isNode = anchor.parentNode;
                var offset = isNode ? $(anchor).offset() :{
                    left:anchor.pageX,
                    top:anchor.pageY
                };
                anchor = isNode ? anchor :anchor.target;
                var ownerDocument = anchor.ownerDocument;
                var defaultView = ownerDocument.defaultView || ownerDocument.parentWindow;
                if (defaultView == window) {
                    return offset;
                }
                var frameElement = defaultView.frameElement;
                var $ownerDocument = $(ownerDocument);
                var docLeft = $ownerDocument.scrollLeft();
                var docTop = $ownerDocument.scrollTop();
                var frameOffset = $(frameElement).offset();
                var frameLeft = frameOffset.left;
                var frameTop = frameOffset.top;
                return {
                    left:offset.left + frameLeft - docLeft,
                    top:offset.top + frameTop - docTop
                };
            }
        });
        Popup.zIndex = 1024;
        Popup.current = null;
        return Popup;
    });
    define("dialog-config", {
        fixed:true,
        zIndex:3e3,
        backdropBackground:"#000",
        backdropOpacity:.7,
        content:"",
        contentType:"",
        closeTime:3e3,
        title:"",
        statusbar:"",
        button:null,
        ok:null,
        cancel:null,
        okValue:"确定",
        cancelValue:"取消",
        cancelDisplay:true,
        width:"",
        height:"",
        padding:"",
        titlePadding:"",
        contentBlock:false,
        skin:"ax-popup",
        boxSkin:"ui-popup-full fat-title",
        quickClose:false,
        cssUri:"",
        innerHTML:'<div i="dialog" class="ui-dialog">' + '<div class="ui-dialog-arrow-a"></div>' + '<div class="ui-dialog-arrow-b"></div>' + '<table class="ui-dialog-grid">' + "<tr>" + '<td i="header" class="ui-dialog-header">' + '<button i="close" class="ui-dialog-close">&#215;</button>' + '<div i="title" class="ui-dialog-title"></div>' + "</td>" + "</tr>" + "<tr>" + '<td i="body" class="ui-dialog-body">' + '<div i="content" class="ui-dialog-content"></div>' + "</td>" + "</tr>" + "<tr>" + '<td i="footer" class="ui-dialog-footer">' + '<div i="statusbar" class="ui-dialog-statusbar"></div>' + '<div i="button" class="ui-dialog-button"></div>' + "</td>" + "</tr>" + "</table>" + "</div>"
    });
    define("dialog", function(require) {
        var $ = require("jquery");
        var Popup = require("popup");
        var defaults = require("dialog-config");
        var css = defaults.cssUri;
        if (css) {
            var fn = require[require.toUrl ? "toUrl" :"resolve"];
            if (fn) {
                css = fn(css);
                css = '<link rel="stylesheet" href="' + css + '" />';
                if ($("base")[0]) {
                    $("base").before(css);
                } else {
                    $("head").append(css);
                }
            }
        }
        var _count = 0;
        var _expando = new Date() - 0;
        var _isIE6 = !("minWidth" in $("html")[0].style);
        var _isMobile = "createTouch" in document && !("onmousemove" in document) || /(iPhone|iPad|iPod)/i.test(navigator.userAgent);
        var _isFixed = !_isIE6 && !_isMobile;
        var artDialog = function(options, ok, cancel) {
            var originalOptions = options = options || {};
            if (typeof options === "string" || options.nodeType === 1) {
                options = {
                    content:options,
                    fixed:!_isMobile
                };
            }
            options = $.extend(true, {}, artDialog.defaults, options);
            options.original = originalOptions;
            var id = options.id = options.id || _expando + _count;
            var api = artDialog.get(id);
            if (api) {
                return api.focus();
            }
            if (!_isFixed) {
                options.fixed = false;
            }
            if (options.quickClose) {
                options.modal = true;
                options.backdropOpacity = .7;
            }
            if (!$.isArray(options.button)) {
                options.button = [];
            }
            if (cancel !== undefined) {
                options.cancel = cancel;
            }
            if (options.cancel) {
                options.button.push({
                    id:"cancel",
                    value:options.cancelValue,
                    callback:options.cancel,
                    display:options.cancelDisplay
                });
            }
            if (ok !== undefined) {
                options.ok = ok;
            }
            if (options.ok) {
                options.button.push({
                    id:"ok",
                    value:options.okValue,
                    callback:options.ok,
                    autofocus:true
                });
            }
            return artDialog.list[id] = new artDialog.create(options);
        };
        var popup = function() {};
        popup.prototype = Popup.prototype;
        var prototype = artDialog.prototype = new popup();
        artDialog.create = function(options) {
            var that = this;
            $.extend(this, new Popup());
            var originalOptions = options.original;
            var $popup = $(this.node).html(options.innerHTML);
            var $backdrop = $(this.backdrop);
            this.options = options;
            this._popup = $popup;
            $.each(options, function(name, value) {
                if (typeof that[name] === "function") {
                    that[name](value);
                } else {
                    that[name] = value;
                }
            });
            if (options.zIndex) {
                Popup.zIndex = options.zIndex;
            }
            $popup.attr({
                "aria-labelledby":this._$("title").attr("id", "title:" + this.id).attr("id"),
                "aria-describedby":this._$("content").attr("id", "content:" + this.id).attr("id")
            });
            this._$("close").css("display", this.cancel === false ? "none" :"").attr("title", this.cancelValue).on("click", function(event) {
                that._trigger("cancel");
                event.preventDefault();
            });
            if (this.contentType != "") {
                this.boxSkin = "";
                if (this.skin == "bk-popup") {
                    this.skin = "bk-popup smallTips";
                } else {
                    this.skin = "smallTips";
                }
            }
            this._$("dialog").addClass(this.skin);
            $popup.addClass(this.boxSkin);
            this._$("body").css("padding", this.padding);
            this._$("title").css("padding", this.titlePadding);
            if (this.contentBlock) {
                this._$("content").css("display", "block");
            }
            if (options.quickClose) {
                $backdrop.on("onmousedown" in document ? "mousedown" :"click", function() {
                    that._trigger("cancel");
                    return false;
                });
            }
            this.addEventListener("show", function() {
                $backdrop.css({
                    opacity:options.backdropOpacity,
                    background:options.backdropBackground
                });
            });
            this._esc = function(event) {
                var target = event.target;
                var nodeName = target.nodeName;
                var rinput = /^input|textarea$/i;
                var isTop = Popup.current === that;
                var keyCode = event.keyCode;
                if (!isTop || rinput.test(nodeName) && target.type !== "button") {
                    return;
                }
                if (keyCode === 27) {
                    that._trigger("cancel");
                }
            };
            $(document).on("keydown", this._esc);
            this.addEventListener("remove", function() {
                $(document).off("keydown", this._esc);
                delete artDialog.list[this.id];
            });
            _count++;
            artDialog.oncreate(this);
            return this;
        };
        artDialog.create.prototype = prototype;
        $.extend(prototype, {
            content:function(html) {
                var $content = this._$("content");
                if (typeof html === "object") {
                    html = $(html);
                    $content.empty("").append(html.show());
                    this.addEventListener("beforeremove", function() {
                        $("body").append(html.hide());
                    });
                } else {
                    if (this.options.contentType != "") {
                        switch (this.options.contentType) {
                          case "load":
                            if (this.options.content == "") {
                                html = '<span class="ui-dialog-loading"></span><p style="float:left;">加载中，请稍候...</p>';
                            } else {
                                html = '<span class="ui-dialog-loading"></span><p style="float:left;">' + this.options.content + "</p>";
                            }
                            break;

                          case "jsonload":
                            if (this.options.content == "") {
                                html = '<span class="ui-dialog-loading"></span><p style="float:left;">数据载入中，请稍候...</p>';
                            } else {
                                html = '<span class="ui-dialog-loading"></span><p style="float:left;">' + this.options.content + "</p>";
                            }
                            break;

                          case "tipsbox":
                            var th = this;
                            if (this.options.closeTime != 0) {
                                setTimeout(function() {
                                    artDialog.list[th.id].close().remove();
                                }, this.options.closeTime);
                            }
                            break;
                        }
                    }
                    $content.html(html);
                }
                return this.reset();
            },
            title:function(text) {
                this._$("title").html(text);
                this._$("header")[text ? "show" :"hide"]();
                return this;
            },
            width:function(value) {
                this._$("content").css("width", value);
                return this.reset();
            },
            height:function(value) {
                this._$("content").css("height", value);
                return this.reset();
            },
            button:function(args) {
                args = args || [];
                var that = this;
                var html = "";
                var number = 0;
                this.callbacks = {};
                if (typeof args === "string") {
                    html = args;
                    number++;
                } else {
                    $.each(args, function(i, val) {
                        var id = val.id = val.id || val.value;
                        var style = "";
                        that.callbacks[id] = val.callback;
                        if (val.display === false) {
                            style = ' style="display:none"';
                        } else {
                            number++;
                        }
                        html += "<button" + ' type="button"' + ' i-id="' + id + '"' + style + (val.disabled ? " disabled" :"") + (val.autofocus ? ' autofocus class="ui-dialog-autofocus"' :"") + "><span>" + val.value + "</span></button>";
                        that._$("button").on("click", "[i-id=" + id + "]", function(event) {
                            var $this = $(this);
                            if (!$this.attr("disabled")) {
                                that._trigger(id);
                            }
                            event.preventDefault();
                        });
                    });
                }
                this._$("button").html(html);
                this._$("footer")[number ? "show" :"hide"]();
                return this;
            },
            statusbar:function(html) {
                this._$("statusbar").html(html)[html ? "show" :"hide"]();
                return this;
            },
            _$:function(i) {
                return this._popup.find("[i=" + i + "]");
            },
            _trigger:function(id) {
                var fn = this.callbacks[id];
                return typeof fn !== "function" || fn.call(this) !== false ? this.close().remove() :this;
            }
        });
        artDialog.oncreate = $.noop;
        artDialog.getCurrent = function() {
            return Popup.current;
        };
        artDialog.get = function(id) {
            return id === undefined ? artDialog.list :artDialog.list[id];
        };
        artDialog.list = {};
        artDialog.defaults = defaults;
        return artDialog;
    });
    define("drag", function(require) {
        var $ = require("jquery");
        var $window = $(window);
        var $document = $(document);
        var isTouch = "createTouch" in document;
        var html = document.documentElement;
        var isIE6 = !("minWidth" in html.style);
        var isLosecapture = !isIE6 && "onlosecapture" in html;
        var isSetCapture = "setCapture" in html;
        var types = {
            start:isTouch ? "touchstart" :"mousedown",
            over:isTouch ? "touchmove" :"mousemove",
            end:isTouch ? "touchend" :"mouseup"
        };
        var getEvent = isTouch ? function(event) {
            if (!event.touches) {
                event = event.originalEvent.touches.item(0);
            }
            return event;
        } :function(event) {
            return event;
        };
        var DragEvent = function() {
            this.start = $.proxy(this.start, this);
            this.over = $.proxy(this.over, this);
            this.end = $.proxy(this.end, this);
            this.onstart = this.onover = this.onend = $.noop;
        };
        DragEvent.types = types;
        DragEvent.prototype = {
            start:function(event) {
                event = this.startFix(event);
                $document.on(types.over, this.over).on(types.end, this.end);
                this.onstart(event);
                return false;
            },
            over:function(event) {
                event = this.overFix(event);
                this.onover(event);
                return false;
            },
            end:function(event) {
                event = this.endFix(event);
                $document.off(types.over, this.over).off(types.end, this.end);
                this.onend(event);
                return false;
            },
            startFix:function(event) {
                event = getEvent(event);
                this.target = $(event.target);
                this.selectstart = function() {
                    return false;
                };
                $document.on("selectstart", this.selectstart).on("dblclick", this.end);
                if (isLosecapture) {
                    this.target.on("losecapture", this.end);
                } else {
                    $window.on("blur", this.end);
                }
                if (isSetCapture) {
                    this.target[0].setCapture();
                }
                return event;
            },
            overFix:function(event) {
                event = getEvent(event);
                return event;
            },
            endFix:function(event) {
                event = getEvent(event);
                $document.off("selectstart", this.selectstart).off("dblclick", this.end);
                if (isLosecapture) {
                    this.target.off("losecapture", this.end);
                } else {
                    $window.off("blur", this.end);
                }
                if (isSetCapture) {
                    this.target[0].releaseCapture();
                }
                return event;
            }
        };
        DragEvent.create = function(elem, event) {
            var $elem = $(elem);
            var dragEvent = new DragEvent();
            var startType = DragEvent.types.start;
            var noop = function() {};
            var className = elem.className.replace(/^\s|\s.*/g, "") + "-drag-start";
            var minX;
            var minY;
            var maxX;
            var maxY;
            var api = {
                onstart:noop,
                onover:noop,
                onend:noop,
                off:function() {
                    $elem.off(startType, dragEvent.start);
                }
            };
            dragEvent.onstart = function(event) {
                var isFixed = $elem.css("position") === "fixed";
                var dl = $document.scrollLeft();
                var dt = $document.scrollTop();
                var w = $elem.width();
                var h = $elem.height();
                minX = 0;
                minY = 0;
                maxX = isFixed ? $window.width() - w + minX :$document.width() - w;
                maxY = isFixed ? $window.height() - h + minY :$document.height() - h;
                var offset = $elem.offset();
                var left = this.startLeft = isFixed ? offset.left - dl :offset.left;
                var top = this.startTop = isFixed ? offset.top - dt :offset.top;
                this.clientX = event.clientX;
                this.clientY = event.clientY;
                $elem.addClass(className);
                api.onstart.call(elem, event, left, top);
            };
            dragEvent.onover = function(event) {
                var left = event.clientX - this.clientX + this.startLeft;
                var top = event.clientY - this.clientY + this.startTop;
                var style = $elem[0].style;
                left = Math.max(minX, Math.min(maxX, left));
                top = Math.max(minY, Math.min(maxY, top));
                style.left = left + "px";
                style.top = top + "px";
                api.onover.call(elem, event, left, top);
            };
            dragEvent.onend = function(event) {
                var position = $elem.position();
                var left = position.left;
                var top = position.top;
                $elem.removeClass(className);
                api.onend.call(elem, event, left, top);
            };
            dragEvent.off = function() {
                $elem.off(startType, dragEvent.start);
            };
            if (event) {
                dragEvent.start(event);
            } else {
                $elem.on(startType, dragEvent.start);
            }
            return api;
        };
        return DragEvent;
    });
    define("dialog-plus", function(require) {
        var $ = require("jquery");
        var dialog = require("dialog");
        var drag = require("drag");
        dialog.oncreate = function(api) {
            var options = api.options;
            var originalOptions = options.original;
            var url = options.url;
            var oniframeload = options.oniframeload;
            var $iframe;
            if (url) {
                this.padding = options.padding = 0;
                $iframe = $("<iframe />");
                $iframe.attr({
                    src:url,
                    name:api.id,
                    width:"100%",
                    height:"100%",
                    allowtransparency:"yes",
                    frameborder:"no",
                    scrolling:"no"
                }).on("load", function() {
                    var test;
                    try {
                        test = $iframe[0].contentWindow.frameElement;
                    } catch (e) {}
                    if (test) {
                        if (!options.width) {
                            api.width($iframe.contents().width());
                        }
                        if (!options.height) {
                            api.height($iframe.contents().height());
                        }
                    }
                    if (oniframeload) {
                        oniframeload.call(api);
                    }
                });
                api.addEventListener("beforeremove", function() {
                    $iframe.attr("src", "about:blank").remove();
                }, false);
                api.content($iframe[0]);
                api.iframeNode = $iframe[0];
            }
            if (!(originalOptions instanceof Object)) {
                var un = function() {
                    api.close().remove();
                };
                for (var i = 0; i < frames.length; i++) {
                    try {
                        if (originalOptions instanceof frames[i].Object) {
                            $(frames[i]).one("unload", un);
                            break;
                        }
                    } catch (e) {}
                }
            }
            $(api.node).on(drag.types.start, "[i=title]", function(event) {
                if (!api.follow) {
                    api.focus();
                    drag.create(api.node, event);
                }
            });
        };
        dialog.get = function(id) {
            if (id && id.frameElement) {
                var iframe = id.frameElement;
                var list = dialog.list;
                var api;
                for (var i in list) {
                    api = list[i];
                    if (api.node.getElementsByTagName("iframe")[0] === iframe) {
                        return api;
                    }
                }
            } else if (id) {
                return dialog.list[id];
            }
        };
        return dialog;
    });
    window.dialog = require("dialog-plus");
}();