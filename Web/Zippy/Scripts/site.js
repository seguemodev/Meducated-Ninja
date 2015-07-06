$(function () {
    $.fn.center = function (options) {
        var settings = $.extend({ position: 'absolute' }, options);
        this.css({ top: '50%', left: '50%', margin: '-' + (this.height() / 2) + 'px 0 0 -' + (this.width() / 2) + 'px' });
        this.css("position", settings.position);
        return this;
    };

    $(".navbar-nav li a").click(function (event) {
        if ($('#zippy-navbar').hasClass('in')) {
            $(".navbar-collapse").collapse('hide');
        }
    });

    $.Zippy = function (contentUrl, apiUrl, fdaApiKey) {
        var searchUrl = apiUrl.replace('_API_', 'Term');
        var userUrl = apiUrl.replace('_API_', 'User');
        var cabinetUrl = apiUrl.replace('_API_', 'Cabinet');
        var medicationUrl = apiUrl.replace('_API_', 'Medication');

        var cache = {
            searchResults: {},
            cabinets: {},
            medications: {}
        };
        var user = null;

        /* SEARCH */
        var terms = new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('term'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            limit: 10,
            remote: {
                url: searchUrl + '/%QUERY',
                filter: function (list) {
                    return $.map(list, function (term) {
                        return { term: term };
                    });
                }
            }
        });
        terms.initialize();

        $('#search-term').typeahead(null, {
            name: 'term',
            displayKey: 'term',
            source: terms.ttAdapter()
        }).on('click', function (ev) {
            var code = ev.keyCode || ev.which;
            if (code == 13) {
                $('#search-btn').click();
            }
        });

        $('#search-btn').on('click', function (e) {
            e.preventDefault();
            var searchVal = $('#search-term').val();
            if (searchVal) {
                processing('Searching FDA Database');
                $.ajax({
                    url: 'https://api.fda.gov/drug/label.json?api_key=' + fdaApiKey + '&search=brand_name:"' + searchVal + '"+generic_name:"' + searchVal + '"+openfda.product_ndc:"' + searchVal + '"&limit=25',
                    datatype: 'JSON',
                    error: function (err) {
                        if (err.status == 404) {
                            $('#search-initial').hide();
                            $('#search-results').hide();
                            $('#no-results').show();
                        } else {
                            alert(err.statusText);
                        }
                    },
                    success: loadSearchResults,
                    complete: processed
                });
            }
        });

        $('#result-selector').on('click', 'a', function (e) {
            e.preventDefault();
            var selected = $(this).attr('data-id');
            var medication = cache.searchResults[selected];
            if (medication) {
                $('#medication-back').attr('href', '#search-page').find('#back-text').text('Search Results');
                showMedication(medication);
            }
        });

        function loadSearchResults(data) {
            cache.searchResults = {};
            $('#search-initial').hide();
            if (data && data.results && data.results.length) {
                var resultSelector = $('#result-selector');
                resultSelector.children().remove();
                $.each(data.results, function () {
                    var medication = this;
                    if (medication && medication.openfda) {
                        cache.searchResults[medication.id] = medication;
                        var properName = medication.openfda.brand_name;
                        if (medication.openfda.brand_name != medication.openfda.generic_name) {
                            //properName += " (" + medication.openfda.generic_name + ")";
                        }
                        if (medication.openfda.manufacturer_name) {
                            properName += " - " + medication.openfda.manufacturer_name;
                        }
                        resultSelector.append($('<div class="row">').append(
                            ($('<a href="#" />').html(properName)).attr('data-id', medication.id)
                        ));
                    }
                });
                $('#search-term-label').text($('#search-term').val());
                $('#no-results').hide();
                $('#search-results').show();
            } else {
                $('#search-results').hide();
                $('#no-results').show();
            }
        }

        /* User */

        $('#login-dialog').autoModal({
            title: 'Login',
            show: false,
            buttons: {
                'New User': function () {
                    processing('Creating Account');
                    $.ajax({
                        url: userUrl,
                        method: 'POST',
                        error: function () {
                            alert('Unable to create a user account');
                        },
                        success: function (data) {
                            user = {
                                Id: data.Id
                            };
                            loginSuccessful();
                        },
                        complete: processed
                    });
                },
                Login: function () {
                    user = {
                        Id: $('#user-id').val()
                    }
                    if (user.Id) {
                        processing('Logging in');
                        $.ajax({
                            url: userUrl + '/' + user.Id,
                            method: 'HEAD',
                            error: function () {
                                $('#user-id-val').show();
                            },
                            success: function (data) {
                                loginSuccessful();
                            },
                            complete: processed
                        });
                    } else {
                        $('#user-id-val').show();
                    }
                },
                Cancel: 'hide'
            }
        });

        $('#login').on('click', function () {
            $('#login-dialog').autoModal('show');
        });

        $('#logout').on('click', function () {
            $('#current-user-id').text('');
            $('#logged-out').show();
            $('#logged-in').hide();
            $('a[href="#search-page"]').tab('show');
            user = null;
        });

        function loginSuccessful() {
            $('#current-user-id').text(user.Id);
            $('#logged-out').hide();
            $('#logged-in').show();
            $('#login-dialog').autoModal('hide');
            var trigger = $('#login-dialog').data('trigger');
            if (trigger) {
                $('#login-dialog').data('trigger', null);
                trigger.currentTarget.click();
            }
            loadCabinets();
        }

        $('body').on('click', '.login-required', function (e) {
            if (!user) {
                e.preventDefault();
                e.stopPropagation();
                $('#user-id').val('');
                $('#user-id-val').hide();
                $('#login-dialog')
                    .data('trigger', e)
                    .autoModal('show');
            }
        });


        /* Cabinets */

        function loadCabinets() {
            $('#cabinets').children().remove();
            $('#cabinet-selector').children().remove();

            if (user.Id) {
                processing('Loading Medicine Cabinets');
                $.ajax({
                    url: userUrl + '/' + user.Id,
                    method: 'GET',
                    success: function (data) {
                        user = data;
                        cache.cabinets = {};
                        $.each(data.Cabinets, function () {
                            addCabinet(this);
                        });
                    },
                    complete: processed
                });
            }
        }

        function addCabinet(cabinet) {
            var row = $('<div class="cabinet row" data-id="' + cabinet.Id + '">');
            row.append($('<div class="cabinet-image-wrapper">')
                .append('<img src="' + contentUrl + '/Images/default-profileicon.png" alt="user picture">'));
            row.append('<div class="cabinet-name">' + cabinet.Name + '</div>');
            $('#cabinets').append(row);
            $('#cabinet-selector').append(row.clone());
            cache.cabinets[cabinet.Id] = cabinet;
            $.each(cabinet.Medications, function () {
                cache.medications[this.Id] = this;
            });
        }

        function saveMedication(cabinet) {
            if (cabinet.id) {
                var medication = cache.searchResults[$('#medication-details').attr('data-id')];
                if (medication) {
                    processing('Adding Medication to Cabinet');
                    $.ajax({
                        url: medicationUrl,
                        method: 'POST',
                        data: {
                            CabinetId: cabinet.id,
                            GenericName: "" + medication.openfda.generic_name,
                            BrandName: "" + medication.openfda.brand_name,
                            Manufacturer: "" + medication.openfda.manufacturer_name,
                            FdaId: medication.id,
                            ImageUrl: medication.imageUrl,
                        },
                        error: function () {
                            alert('Unable to save medication to your cabinet');
                        },
                        success: function (data) {
                            cache.cabinets[cabinet.id].Medications.push(data);
                            alert('Medication Saved');
                        },
                        complete: processed
                    });
                } else {
                    alert('Unable to save medication to your cabinet');
                }
            } else {
                createCabinet(function (data) {
                    saveMedication({ id: data.Id });
                });
            }
        }

        function createCabinet(callback) {
            processing('Creating Cabinet');
            $.ajax({
                url: cabinetUrl,
                method: 'POST',
                data: {
                    UserId: user.Id,
                    Name: $('#cabinet-name').val()
                },
                error: function () {
                    alert('Unable create your cabinet');
                },
                success: function (data) {
                    addCabinet(data);
                    if (callback) {
                        callback(data);
                    } else {
                        alert('Cabinet Created');
                    }
                },
                complete: processed
            });
        }

        $('#cabinets').on('click', '.cabinet', function (e) {
            e.preventDefault();
            var selected = $(this).attr('data-id');
            var cabinet = cache.cabinets[selected];
            if (cabinet) {
                $('.cabinet-list').children().addClass('show-medication');
                var $cabinetContents = $('#cabinet-contents');
                $cabinetContents.children().remove();
                cache.medications = {};
                var closeCabinet = $('<a href="#" id="close-cabinet">&lt; Back to Cabinets</span>');
                closeCabinet.on('click', function (e) {
                    e.preventDefault();
                    $('.cabinet-list').children().removeClass('show-medication');
                });
                var row = $('<div class="row">')
                    .append($('<span class="cabinet-name">').text(cabinet.Name))
                    .append(closeCabinet);
                $cabinetContents.append(row);
                $.each(cabinet.Medications, function () {
                    var row = $('<div class="medication row" data-id="' + this.Id + '">');
                    row.append($('<div class="col-xs-3"></div>')
                        .append(this.ImageUrl ? $('<img src="' + this.ImageUrl + '" alt="Pill image">') : null));
                    row.append($('<div class="col-xs-9">')
                        .append($('<div class="medication-name"><a href="#" data-id="' + this.FdaId + '">' + this.BrandName + '</a></div>')
                            .append($('<a href="#" class="remove-medication">Remove</a>')))
                        .append($('<div class="manufacturer-name">' + this.Manufacturer + '</div>')));
                    $cabinetContents.append(row);
                    cache.medications[this.Id] = this;
                });
            }
        });

        $('#cabinet-contents').on('click', '.medication-name a', function (e) {
            e.preventDefault();
            var id = $(this).attr('data-id');
            if (cache.searchResults[id]) {
                $('#medication-back').attr('href', '#cabinet-page').find('#back-text').text('Medicine Cabinet');
                showMedication(cache.searchResults[id]);
            } else {
                processing('Searching FDA Database');
                $.ajax({
                    url: 'https://api.fda.gov/drug/label.json?api_key=' + fdaApiKey + '&search=id:' + id,
                    datatype: 'JSON',
                    success: function (data) {
                        var medication = data.results[0];
                        cache.searchResults[id] = medication;
                        $('#medication-back').attr('href', '#cabinet-page').find('#back-text').text('Medicine Cabinet');
                        showMedication(medication);
                    },
                    complete: processed
                });
            }
        });

        $('#cabinet-contents').on('click', 'a.remove-medication', function () {
            var row = $(this).closest('.medication');
            var medication = cache.medications[row.attr('data-id')];
            if (medication) {
                processing('Removing Medication from Cabinet');
                $.ajax({
                    url: medicationUrl + '/' + medication.Id,
                    method: 'DELETE',
                    success: function () {
                        row.remove();
                    },
                    complete: processed
                });
            }
        });

        $('#save-to-cabinet').on('click', function (e) {
            if (!user) return;
            if (user.Id) {
                $('#cabinet-dialog').autoModal('show');
            } else {
                $('#cabinet-name-dialog').attr('data-triggeredBy','saveMedication').autoModal('show');
            }
        });

        $('#create-cabinet').on('click', function (e) {
            $('#cabinet-name-dialog').attr('data-triggeredBy','createCabinet').autoModal('show');
        });

        $('#cabinet-dialog').autoModal({
            title: 'Medicine Cabinets',
            show: false,
            buttons: {
                Cancel: 'hide'
            }
        });

        $('#cabinet-dialog').on('click', '.cabinet', function (e) {
            e.preventDefault();
            $('#cabinet-dialog').autoModal('hide');
            if ($(this).attr('data-id')) {
                saveMedication({ id: $(this).attr('data-id') });
            } else {
                $('#cabinet-name-dialog').autoModal('show');
            }
        });

        $('#cabinet-name-dialog').autoModal({
            title: 'New Medicine Cabinet',
            show: false,
            buttons: {
                Create: function () {
                    $('#cabinet-name-dialog').autoModal('hide');
                    if ($('#cabinet-name-dialog').attr('data-triggeredBy') == 'saveMedication') {
                        saveMedication({ name: $('#cabinet-name').text() });
                    } else {
                        createCabinet();
                    }
                },
                Cancel: 'hide'
            }
        });

        /* Medications */
        $('#pill-image').on('click', function (e) {
            $('#pill-image-dialog-modal .modal-title').text($('#brand-name').text());
        });
        $('#pill-image-dialog').autoModal({ show: false });


        var medicationTabs = [
            { label: 'Indications and Usage', fields: ['indications_and_usage'] },
            { label: 'Dosage', fields: ['dosage_and_administration'] },
            { label: 'Patient Info', fields: ['information_for_patients'] },
            { label: 'Contraindications', fields: ['contraindications'] },
            { label: 'How Supplied', fields: ['how_supplied'] },
            { label: 'Pediatric Use', fields: ['pediatric_use'] },
            { label: 'Precautions', fields: ['precautions', 'general_precautions'], firstOnly: true },
            { label: 'Warnings', fields: ['boxed_warning', 'warnings', 'warnings_table', 'warnings_and_cautions', 'warnings_and_cautions_table'] },
            { label: 'Overdosage', fields: ['overdosage'] },
            { label: 'Adverse Reactions', fields: ['adverse_reactions', 'adverse_reactions_table'] },
            { label: 'Drug Interactions', fields: ['drug_interactions'] },
            { label: 'Pregnancy and Nursing', fields: ['pregnancy', 'nursing_mothers'] },
            { label: 'Pharmacology', fields: ['clinical_pharmacology'] },
        ];
        function showMedication(medication) {
            if (medication.imageUrl) {
                setPillImage(medication.imageUrl);
            } else {
                loadImage(medication);
            }
            $('#brand-name').text(medication.openfda.brand_name);
            $('#manufacturer-name').text(medication.openfda.manufacturer_name)
            var tabContainer = $('#medication-tabs');
            var contentContainer = $('#medication-tab-content');
            contentContainer.children().remove();
            tabContainer.children().remove();
            $.each(medicationTabs, function (index) {
                loadText(tabContainer, contentContainer, medication, index);
            })
            $('#medication-tabs').tabCollapse();
            $('#medication-details').attr('data-id', medication.id);
            $('#medication-tab').tab('show');
        }

        function setPillImage(url) {
            $('#pill-image').attr('src', url);
            $('#pill-image-dialog img').attr('src', url.replace("/120/", "/600/"));
        }

        function loadText(tabContainer, contentContainer, medication, index) {
            var tab = medicationTabs[index];
            var foundContent = false;
            // <li id="information-tab" class="active"><a href="#information" data-toggle="tab">Patient Information</a></li>
            var tabControl = $('<li/>').attr('id', tab.fields[0] + '_tab');
            tabControl.append($('<a data-toggle="tab"/>').text(tab.label).attr('href', '#' + tab.fields[0]));

            //<div class="tab-pane fade in active" id="information"></div>
            var tabPanel = $('<div class="tab-pane fade"/>').attr('id', tab.fields[0]);
            $.each(tab.fields, function () {
                if (tab.firstOnly && foundContent) {
                    return;
                }
                var fieldValue = medication.openfda[this] || medication[this];
                if (fieldValue) {
                    foundContent = true;
                    tabPanel.append($('<div/>').html(fieldValue));
                }
            });
            if (!foundContent) {
                return;
            }

            if (index == 0) {
                tabControl.addClass('active');
                tabPanel.addClass('in active');
            }
            tabContainer.append(tabControl);
            contentContainer.append(tabPanel);
        }

        function loadImage(medication, index, subIndex) {
            var field;
            var value;
            index = index || 0;
            switch (index) {
                case 0:
                    subIndex = null;
                    field = 'setid';
                    value = medication.openfda.spl_set_id;
                    break;
                case 1:
                    subIndex = null;
                    if (medication.openfda.spl_set_id != medication.openfda.set_id) {
                        field = 'setid';
                        value = medication.openfda.set_id;
                    }
                    break;
                case 2:
                    subIndex = subIndex || 0;
                    field = 'ndc';
                    value = (medication.openfda.product_ndc && medication.openfda.product_ndc.length > subIndex) ?
                        medication.openfda.product_ndc[subIndex++] : null;
                    if (!value) {
                        subIndex = null;
                    }
                    subIndex = null;
                    break;
                case 3:
                    subIndex = subIndex || 0;
                    field = 'rxcui';
                    value = (medication.openfda.rxcui && medication.openfda.rxcui.length > subIndex) ?
                        medication.openfda.rxcui[subIndex++] : null;
                    if (!value) {
                        subIndex = null;
                    }
                    break;
                default:
                    return;
            }
            if (!subIndex) {
                index += 1;
            }
            if (field && value) {
                $.ajax({
                    url: 'http://rximage.nlm.nih.gov/api/rximage/1/rxnav?resolution=120&' + field + '=' + value,
                    method: 'GET',
                    dataType: 'json',
                    error: function () {
                        loadImage(medication, index, subIndex);
                    },
                    success: function (data) {
                        if (data.replyStatus && data.replyStatus.success && data.replyStatus.imageCount) {
                            medication.imageUrl = data.nlmRxImages[0].imageUrl;
                            $('#pill-image').attr('src', medication.imageUrl);
                            $('#pill-image-dialog img').attr('src', medication.imageUrl.replace("/120/", "/600/"));
                        } else {
                            loadImage(medication, index, subIndex);
                        }
                    }
                });
            } else {
                loadImage(medication, index, subIndex);
            }
        }


        /* Utilities */

        var processingCaptions = [];
        function processing (caption) {
            var loading = $('#ajax-loading');
            if (!loading.length) {
                loading = $('<div/>').attr('id', 'ajax-loading').addClass('ajax-loading modal-content');
                loading.append($('<label/>').addClass('ajax-spinner'));
                $('body').append(loading);
                loading = $('#ajax-loading');
            }
            var zmax = 0;
            var oldCaption = loading.find('label').text();
            if (oldCaption) {
                processingCaptions.push(oldCaption);
            }
            loading.siblings().add($('.modal')).each(function () {
                var cur = parseInt($(this).css('zIndex'));
                zmax = cur > zmax ? cur : zmax;
            });
            caption = (caption ? caption : 'Loading') + ', please wait...';
            loading.find('label').text(caption);
            loading.center({ position: 'fixed' }).css('zIndex', zmax + 1);
        }

        function processed() {
            if (processingCaptions.length) {
                $('#ajax-loading label').text(processingCaptions.pop());
            } else {
                $('#ajax-loading label').text('');
                $('#ajax-loading').hide().css('zIndex', 0);
            }
        }
    };
});
