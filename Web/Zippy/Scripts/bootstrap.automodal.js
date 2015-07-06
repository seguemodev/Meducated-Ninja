/*!
 * Bootstrap AutoModal v0.1 
 * Copyright 2015 Segue Technologies, Inc. (http://www.seguetech.com)
 * Licensed under MPL 2.0 (https://www.mozilla.org/MPL/2.0/)
 */

(function ($) {

    if (!$.autoModal) {
        $.autoModal = {
            defaults: {
                // modal options
                show: false,
                backdrop: 'static',
                keyboard: true,
                width: undefined,
                height: undefined,
                class: undefined,
                showCloseButton: true,
                buttons: { OK: 'hide' },
                buttonDefaults: {
                    buttonType: 'btn-primary',
                    visible: true,
                },
            },

            _init: function (element, options) {
                $.each(element, function () {
                    var settings;
                    if (typeof options == 'function') {
                        settings = options.apply(this);
                    } else {
                        settings = options;
                    }
                    $.autoModal._buildModal(this, settings);
                    settings = $.extend({}, $.autoModal.defaults, settings);
                    $(this).show().closest('.modal').modal({
                        show: settings.show,
                        backdrop: settings.backdrop,
                        keyboard: settings.keyboard,
                    });
                });
                return element;
            },

            _destroy: function (element) {
                element.unwrap().siblings().remove();
                element.unwrap().unwrap().unwrap();
            },

            _buildModal: function (element, settings) {
                var $element = $(element);
                var container = $element.closest('.modal#' + element.id + '-modal');
                if (container.length) {
                    settings.buttonDefaults = $.extend({}, $.autoModal.defaults.buttonDefaults, settings.buttonDefaults);
                } else {
                    settings = $.extend({}, $.autoModal.defaults, settings);
                    container = $('<div class="modal fade" tabindex="-1" role="dialog" aria-hidden="true"><div class="modal-dialog"><div class="modal-content"><div class="modal-body"></div></div></div></div>');
                    container.attr('id', element.id + '-modal');
                    container.attr('aria-labelledby', container.id);
                    $element.wrap(container);

                    var header = $('<div class="modal-header">');
                    header.append($('<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>'));
                    $('<h4 class="modal-title" id="myModalLabel"></h4>')
                        .text(settings.title || $element.attr('title'))
                        .appendTo(header);
                    var footer = $('<div class="modal-footer"></div>');
                    body = $element.closest('.modal-body');
                    header.insertBefore(body);
                    footer.insertAfter(body);
                    container = $element.closest('.modal#' + element.id + '-modal');
                }
                if (typeof settings.class != 'undefined') {
                    $('.modal-dialog', container).attr('class', 'modal-dialog');
                    if (settings.class) {
                        $('.modal-dialog', container).addClass(settings.class);
                    }
                }
                if (typeof settings.showCloseButton != 'undefined') {
                    $('button.close', container).toggle(settings.showCloseButton);
                }
                if (typeof settings.title != 'undefined') {
                    var title = settings.title || $element.attr('title') || $('h4.modal-title', container).text(title);
                    if (title && (title != '')) {
                        $('h4.modal-title', container).text(title).show();
                    } else {
                        $('h4.modal-title', container).text('').hide();
                    }
                }
                var footer = $('.modal-footer', container);
                if (typeof settings.buttons != 'undefined') {
                    footer.children().remove();
                    $.each(settings.buttons, function (index, options) {
                        if (typeof options != 'object') {
                            options = {
                                click: options
                            };
                        }
                        var buttonSettings = $.extend({}, settings.buttonDefaults, options);
                        if (!buttonSettings.caption) {
                            buttonSettings.caption = index;
                        }
                        var button = $('<button type="button" class="btn">' + buttonSettings.caption + '</button>').appendTo(footer);
                        button.addClass(buttonSettings.buttonType);
                        if (typeof buttonSettings.click == 'string') {
                            button.on('click', function () { $.autoModal._command($element, buttonSettings.click); });
                        } else if (typeof buttonSettings.click == 'function') {
                            button.on('click', function () {
                                var dataArray = buttonSettings.data;
                                if (dataArray && dataArray.constructor !== Array) {
                                    dataArray = [dataArray];
                                }
                                buttonSettings.click.apply(this, dataArray);
                            });
                        }
                        if (buttonSettings.id) {
                            button.attr('id', buttonSettings.id);
                        }
                        if (!buttonSettings.visible) {
                            button.hide();
                        }
                    });
                }
            },
            _command: function (element, cmd) {
                switch (cmd) {
                    case 'destroy':
                        $.autoModal._destroy(element);
                        break;
                    default:
                        element.closest('.modal').modal(cmd);
                }
            },

        };
    }

    $.fn.autoModal = function (options) {
        if (typeof options == 'string') {
            return $.autoModal._command(this, options);
        } else {
            return $.autoModal._init(this, options);
        }
    };

}(jQuery));