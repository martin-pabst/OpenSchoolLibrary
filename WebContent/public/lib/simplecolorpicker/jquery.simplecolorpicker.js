/*!
 * Very simple jQuery Color Picker
 * https://github.com/tkrotoff/jquery-simplecolorpicker
 *
 * Copyright (C) 2012-2013 Tanguy Krotoff <tkrotoff@gmail.com>
 *
 * Licensed under the MIT license
 */

(function ($) {
    'use strict';

    /**
     * Constructor.
     */
    var SimpleColorPicker = function (select, options) {
        this.init('simplecolorpicker', select, options);
    };

    /**
     * SimpleColorPicker class.
     */
    SimpleColorPicker.prototype = {
        constructor: SimpleColorPicker,

        init: function (type, select, options) {
            var self = this;

            self.type = type;

            self.$select = $(select);
            self.$select.hide();

            self.options = $.extend({}, $.fn.simplecolorpicker.defaults, options);

            self.$colorList = null;

            if (self.options.picker === true) {
                var selectText = self.$select.find('> option:selected').text();
                self.$icon = $('<span class="simplecolorpicker icon"'
                    + ' title="' + selectText + '"'
                    + ' style="background-color: ' + self.$select.val() + ';"'
                    + ' role="button" tabindex="0">'
                    + '</span>').insertAfter(self.$select);
                self.$icon.on('click.' + self.type, $.proxy(self.showPicker, self));

                self.$picker = $('<span class="simplecolorpicker picker ' + self.options.theme + '"></span>').appendTo(document.body);
                self.$colorList = self.$picker;

                // Hide picker when clicking outside
                $(document).on('mousedown.' + self.type, $.proxy(self.hidePicker, self));
                self.$picker.on('mousedown.' + self.type, $.proxy(self.mousedown, self));
            } else {
                self.$inline = $('<span class="simplecolorpicker inline ' + self.options.theme + '"></span>').insertAfter(self.$select);
                self.$colorList = self.$inline;
            }

            // Build the list of colors
            // <span class="color selected" title="Green" style="background-color: #7bd148;" role="button"></span>
            self.$select.find('> option').each(function () {
                var $option = $(this);
                var color = $option.val();
                var borderColor = self.changeBrightness(color, 0.6);
                var textColor = self.getTextColor(color);

                var isSelected = $option.is(':selected');
                var isDisabled = $option.is(':disabled');

                var selected = '';
                if (isSelected === true) {
                    selected = ' data-selected';
                }

                var disabled = '';
                if (isDisabled === true) {
                    disabled = ' data-disabled';
                }

                var title = '';
                if (isDisabled === false) {
                    title = ' title="' + $option.text() + '"';
                }

                var role = '';
                if (isDisabled === false) {
                    role = ' role="button" tabindex="0"';
                }

                var $colorSpan = $('<span class="color"'
                    + title
                    + ' style="background-color: ' + color
                    + '; border-color: ' + borderColor
                    + '; color: ' + textColor
                    + '"'
                    + ' data-color="' + color + '"'
                    + ' data-bordercolor="' + borderColor + '"'
                    + ' data-textcolor="' + textColor + '"'
                    + selected
                    + disabled
                    + role + '>'
                    + '</span>');

                self.$colorList.append($colorSpan);
                $colorSpan.on('click.' + self.type, $.proxy(self.colorSpanClicked, self));

                var $next = $option.next();
                if ($next.is('optgroup') === true) {
                    // Vertical break, like hr
                    self.$colorList.append('<span class="vr"></span>');
                }
            });
        },

        /**
         * Changes the selected color.
         *
         * @param color the hexadecimal color to select, ex: '#fbd75b'
         */
        selectColor: function (color) {
            var self = this;

            var $colorSpan = self.$colorList.find('> span.color').filter(function () {
                return $(this).data('color').toLowerCase() === color.toLowerCase();
            });

            if ($colorSpan.length > 0) {
                self.selectColorSpan($colorSpan);
            } else {
                console.log("The given color '" + color + "' could not be found");
            }
        },

        showPicker: function () {
            var pos = this.$icon.offset();
            this.$picker.css({
                // Remove some pixels to align the picker icon with the icons inside the dropdown
                left: pos.left - 6,
                top: pos.top + this.$icon.outerHeight()
            });

            this.$picker.show(this.options.pickerDelay);
        },

        hidePicker: function () {
            this.$picker.hide(this.options.pickerDelay);
        },

        /**
         * Selects the given span inside $colorList.
         *
         * The given span becomes the selected one.
         * It also changes the HTML select value, this will emit the 'change' event.
         */
        selectColorSpan: function ($colorSpan) {
            var color = $colorSpan.data('color');
            var borderColor = $colorSpan.data('bordercolor');
            var textColor = $colorSpan.data('textcolor');
            var title = $colorSpan.prop('title');

            // Mark this span as the selected one
            $colorSpan.siblings().removeAttr('data-selected');
            $colorSpan.attr('data-selected', '');

            if (this.options.picker === true) {
                this.$icon.css('background-color', color);
                this.$icon.prop('title', title);
                this.hidePicker();
            }

            // Change HTML select value
            this.$select.val(color);
            this.$select.data('bordercolor', borderColor);
            this.$select.data('textcolor', textColor);
        },


        getTextColor: function (hex) {
            // strip the leading # if it's there
            hex = hex.replace(/^\s*#|\s*$/g, '');

            // convert 3 char codes --> 6, e.g. `E0F` --> `EE00FF`
            if (hex.length == 3) {
                hex = hex.replace(/(.)/g, '$1$1');
            }

            var r = parseInt(hex.substr(0, 2), 16),
                g = parseInt(hex.substr(2, 2), 16),
                b = parseInt(hex.substr(4, 2), 16);

            var brightness = ( r + g + b ) / 3;

            if (brightness > 130) {
                return '#000000';
            }

            return '#ffffff';
        },


        changeBrightness: function (hex, percent) {
            // strip the leading # if it's there
            hex = hex.replace(/^\s*#|\s*$/g, '');

            // convert 3 char codes --> 6, e.g. `E0F` --> `EE00FF`
            if (hex.length == 3) {
                hex = hex.replace(/(.)/g, '$1$1');
            }

            var r = parseInt(hex.substr(0, 2), 16),
                g = parseInt(hex.substr(2, 2), 16),
                b = parseInt(hex.substr(4, 2), 16);

            r = Math.round(r * percent).toString(16);
            g = Math.round(g * percent).toString(16);
            b = Math.round(b * percent).toString(16);

            if (r.length < 2) {
                r = "0" + r;
            }

            if (g.length < 2) {
                g = "0" + g;
            }

            if (b.length < 2) {
                b = "0" + b;
            }


            return '#' + r + g + b;
            /*
             ((0|(1<<8) + r + (256 - r) * percent / 100).toString(16)).substr(1) +
             ((0|(1<<8) + g + (256 - g) * percent / 100).toString(16)).substr(1) +
             ((0|(1<<8) + b + (256 - b) * percent / 100).toString(16)).substr(1);
             */
        },

        /**
         * ('#000000', 50) --> #808080
         * ('#EEEEEE', 25) --> #F2F2F2
         * ('EEE     , 25) --> #F2F2F2
         **/

        /**
         * The user clicked on a color inside $colorList.
         */
        colorSpanClicked: function (e) {
            // When a color is clicked, make it the new selected one (unless disabled)
            if ($(e.target).is('[data-disabled]') === false) {
                this.selectColorSpan($(e.target));
                this.$select.trigger('change');
            }
        },

        /**
         * Prevents the mousedown event from "eating" the click event.
         */
        mousedown: function (e) {
            e.stopPropagation();
            e.preventDefault();
        },

        destroy: function () {
            if (this.options.picker === true) {
                this.$icon.off('.' + this.type);
                this.$icon.remove();
                $(document).off('.' + this.type);
            }

            this.$colorList.off('.' + this.type);
            this.$colorList.remove();

            this.$select.removeData(this.type);
            this.$select.show();
        }
    };

    /**
     * Plugin definition.
     * How to use: $('#id').simplecolorpicker()
     */
    $.fn.simplecolorpicker = function (option) {
        var args = $.makeArray(arguments);
        args.shift();

        // For HTML element passed to the plugin
        return this.each(function () {
            var $this = $(this),
                data = $this.data('simplecolorpicker'),
                options = typeof option === 'object' && option;
            if (data === undefined) {
                $this.data('simplecolorpicker', (data = new SimpleColorPicker(this, options)));
            }
            if (typeof option === 'string') {
                data[option].apply(data, args);
            }
        });
    };

    /**
     * Default options.
     */
    $.fn.simplecolorpicker.defaults = {
        // No theme by default
        theme: '',

        // Show the picker or make it inline
        picker: false,

        // Animation delay in milliseconds
        pickerDelay: 0,

        colorList: [
            {backgroundColor: "#ac725e", fontColor: "#ffffff", name: "Dunkelbraun"},
            {backgroundColor: "#d06b64", fontColor: "#ffffff", name: "Braun"},
            {backgroundColor: "#f83a22", fontColor: "#ffffff", name: "Rot 1"},
            {backgroundColor: "#fa573c", fontColor: "#ffffff", name: "Rot 2"},
            {backgroundColor: "#ff7537", fontColor: "#000000", name: "Rot 3"},
            {backgroundColor: "#ffad46", fontColor: "#000000", name: "Orange"},
            {backgroundColor: "#42d692", fontColor: "#000000", name: "Mint"},
            {backgroundColor: "#16a765", fontColor: "#ffffff", name: "Dunkelgrün"},
            {backgroundColor: "#7bd148", fontColor: "#000000", name: "Grün"},
            {backgroundColor: "#b3dc6c", fontColor: "#000000", name: "Hellgrün"},
            {backgroundColor: "#fbe983", fontColor: "#000000", name: "Helles Ocker"},
            {backgroundColor: "#fad165", fontColor: "#000000", name: "Ocker"},
            {backgroundColor: "#92e1c0", fontColor: "#000000", name: "Helles Mintgrün"},
            {backgroundColor: "#9fe1e7", fontColor: "#000000", name: "Hellblau"},
            {backgroundColor: "#9fc6e7", fontColor: "#000000", name: "Aqua"},
            {backgroundColor: "#4986e7", fontColor: "#ffffff", name: "Blau 1"},
            {backgroundColor: "#9a9cff", fontColor: "#000000", name: "Blau 2"},
            {backgroundColor: "#b99aff", fontColor: "#000000", name: "Violett 1"},
            {backgroundColor: "#c2c2c2", fontColor: "#000000", name: "Grau 1"},
            {backgroundColor: "#cabdbf", fontColor: "#000000", name: "Grau 2"},
            {backgroundColor: "#cca6ac", fontColor: "#000000", name: "Rotgrau"},
            {backgroundColor: "#f691b2", fontColor: "#000000", name: "Rosa"},
            {backgroundColor: "#cd74e6", fontColor: "#000000", name: "Lila 1"},
            {backgroundColor: "#a47ae2", fontColor: "#000000", name: "Lila 2"}
        ]


    };

})(jQuery);
