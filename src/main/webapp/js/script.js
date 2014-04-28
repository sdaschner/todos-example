/*
 * Copyright (C) 2014 Sebastian Daschner, sebastian-daschner.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

de = {};
de.sd = {};
de.sd.todos = {

    filterQuery: null,
    tasksUrl: '/todos/resources/tasks/',
    filterUrl: '/todos/resources/filters/',

    init: function() {
        de.sd.todos.bindEvents();

        de.sd.todos.loadTasks();
    },

    bindEvents: function() {
        jQuery(document)
            // add task submit
            .on('submit', 'form#addTask', function(ev) {
                ev.preventDefault();

                var input = jQuery('input#addTask_name').val();
                if (!/^[^@]+(@[a-z]+\s*)*$/.test(input)) {
                    jQuery('input#addTask_name').addClass("error");
                    return;
                }

                jQuery('input#addTask_name').removeClass("error");

                var values = de.sd.todos.extractValues(input);

                de.sd.todos.addTask(values.name, values.contexts);
            })
            // add task link
            .on('click', 'a.addTask', function(ev) {
                ev.preventDefault();
                jQuery('form#addTask').trigger('submit');
            })
            // filter tasks submit
            .on('submit', 'form#filterTasks', function(ev) {
                ev.preventDefault();

                var input = jQuery('input#filterTasks_filter').val();
                if (!/^[^@]*(@[a-z]+\s*)*$/.test(input)) {
                    jQuery('input#filterTasks_filter').addClass("error");
                    return;
                }

                jQuery('input#filterTasks_filter').removeClass("error");

                var values = de.sd.todos.extractValues(input);

                de.sd.todos.filterTasks(values.name, values.contexts);
            })
            // filter tasks link
            .on('click', 'a.filter', function(ev) {
                ev.preventDefault();
                jQuery('form#filterTasks').trigger('submit');
            })
            // reset filter link
            .on('click', 'a.resetFilter', function(ev) {
                ev.preventDefault();
                de.sd.todos.loadTasks();
            })
            // filter context link
            .on('click', '.context', function(ev) {
                ev.preventDefault();
                jQuery('form#filterTasks')
                    .find('input[type=text]').val(jQuery(this).html())
                    .end()
                    .trigger('submit');
            })
            // delete task link
            .on('click', 'a.delete', function(ev) {
                ev.preventDefault();
                if (window.confirm("Do you really want to delete task '"+jQuery(this).attr('data-name').trim()+"'?")) {
                    de.sd.todos.deleteTask(jQuery(this).attr('data-id'));
                }
            })
            // finish task link
            .on('click', 'a.finish', function(ev) {
                ev.preventDefault();
                de.sd.todos.finishTask(jQuery(this).attr('data-id'));
            })
            // reactivate task link
            .on('click', 'a.reactivate', function(ev) {
                ev.preventDefault();
                de.sd.todos.reactivateTask(jQuery(this).attr('data-id'));
            })
            // edit task link
            .on('click', 'a.edit', function(ev) {
                ev.preventDefault();
                var $parentNode = jQuery(this).parent(),
                    $taskContent = $parentNode.find('.taskContent'),
                    taskText = $taskContent.text(),
                    taskId = jQuery(this).attr('data-id')
                ;
                $taskContent.remove();
                $parentNode.prepend('<form class="editTask" action="#" data-id="'+taskId+'"><input type="text" /><input class="invisible" type="submit" /></form>');
                $parentNode
                    .find('input[type=text]')
                        .val(taskText)
                    .end()
                    .find('a')
                        .remove()
                    .end()
                        .append('<a class="change button" href="#">Change</a>')
                ;

                // focus on last char
                var $input = $parentNode.find('input[type=text]'),
                    inputLength= $input.val().length;
                $input.focus();
                $input[0].setSelectionRange(inputLength, inputLength);
            })
            // submit edit task submit
            .on('submit', 'form.editTask', function(ev) {
                ev.preventDefault();

                var $this = jQuery(this),
                    $input = $this.find('input[type=text]'),
                    inputText = $input.val()
                ;
                if (!/^[^@]*(@[a-z]+\s*)*$/.test(inputText)) {
                    $input.addClass("error");
                    return;
                }
                $input.removeClass("error");

                var values = de.sd.todos.extractValues(inputText);

                de.sd.todos.changeTask($this.attr('data-id'), values.name, values.contexts);
            })
            // submit edit task link
            .on('click', 'a.change', function(ev) {
                ev.preventDefault();
                jQuery(this).parent().find('form').trigger('submit');
            })
        ;
    },

    addTask: function(name, contexts) {

        var contextString = de.sd.todos.joinContexts(contexts);

        jQuery.ajax({
            type: 'POST',
            url: de.sd.todos.tasksUrl,
            data: '{"name": "'+name+'","contexts": '+contextString+'}',
            contentType: 'application/json',
            error: function() {
                alert('Could not add task.');
            },
            success: function(data, status, jqXHR) {
                if (Math.floor(jqXHR.status / 100) != 2) {
                    // TODO add X-message header
                    alert('Could not add task');
                } else {
                    de.sd.todos.loadTasks();
                    jQuery('input#addTask_name').val('');
                }
            }
        });
    },

    changeTask: function(id, name, contexts) {

        jQuery.ajax({
            type: 'GET',
            url: de.sd.todos.tasksUrl+id,
            error: function() {
                alert('Could not edit task.');
            },
            success: function(data, status, jqXHR) {
                if (Math.floor(jqXHR.status / 100) != 2) {
                    // TODO add X-message header
                    alert('Could not edit task');
                } else {
                    data.name = name;
                    data.contexts = contexts;
                    jQuery.ajax({
                        type: 'PUT',
                        url: de.sd.todos.tasksUrl+id,
                        data: JSON.stringify(data),
                        contentType: 'application/json',
                        error: function() {
                            alert('Could not edit task.');
                        },
                        success: function(data, status, jqXHR) {
                            if (Math.floor(jqXHR.status / 100) != 2) {
                                // TODO add X-message header
                                alert('Could not edit task');
                            } else if (de.sd.todos.filterQuery) {
                                jQuery('input#filterTasks_filter').val(de.sd.todos.filterQuery);
                                jQuery('form#filterTasks').trigger('submit');
                            } else {
                                de.sd.todos.loadTasks();
                            }
                        }
                    });
                }
            }
        });
    },

    filterTasks: function(name, contexts) {

        var contextString = de.sd.todos.joinContexts(contexts);
        jQuery('form#filterTasks a.resetFilter').addClass('hide');

        jQuery.ajax({
            type: 'POST',
            url: de.sd.todos.filterUrl,
            data: '{"text": "'+name+'","contexts": '+contextString+'}',
            contentType: 'application/json',
            error: function() {
                alert('Could not filter tasks.');
            },
            success: function(data, status, jqXHR) {
                if (Math.floor(jqXHR.status / 100) != 2) {
                    // TODO add X-message header
                    alert('Could not filter tasks');
                } else if (data) {
                    de.sd.todos.showTasks(data);
                    de.sd.todos.filterQuery = jQuery('input#filterTasks_filter').val();
                    jQuery('form#filterTasks a.resetFilter').removeClass('hide');
                }
            }
        });

    },

    loadTasks: function() {
        jQuery('div#loading').show();

        // reset filter
        de.sd.todos.filterQuery = null;
        jQuery('form#filterTasks input[type=text]').val('');
        jQuery('form#filterTasks a.resetFilter').addClass('hide');

        jQuery.ajax({
            type: 'GET',
            url: de.sd.todos.tasksUrl,
            error: function() {
                alert('Could not load tasks.');
            },
            success: function(data, status, jqXHR) {
                if (Math.floor(jqXHR.status / 100) != 2) {
                    // TODO add X-message header
                    alert('Could not load task');
                } else if (data) {
                    de.sd.todos.showTasks(data);
                }
            }
        });
    },

    deleteTask: function(id) {
        jQuery.ajax({
            type: 'DELETE',
            url: de.sd.todos.tasksUrl+id,
            error: function() {
                alert('Could not delete task.');
            },
            success: function(data, status, jqXHR) {
                if (Math.floor(jqXHR.status / 100) != 2) {
                    // TODO add X-message header
                    alert('Could not delete task');
                } else if (de.sd.todos.filterQuery) {
                    jQuery('input#filterTasks_filter').val(de.sd.todos.filterQuery);
                    jQuery('form#filterTasks').trigger('submit');
                } else {
                    de.sd.todos.loadTasks();
                }
            }
        });
    },

    finishTask: function(id) {
        de.sd.todos.finishOrReactivateTask(id, true);
    },

    reactivateTask: function(id) {
        de.sd.todos.finishOrReactivateTask(id, false);
    },

    finishOrReactivateTask: function(id, finish) {
        jQuery.ajax({
            type: 'GET',
            url: de.sd.todos.tasksUrl+id,
            error: function() {
                alert('Could not edit task.');
            },
            success: function(data, status, jqXHR) {
                if (Math.floor(jqXHR.status / 100) != 2) {
                    // TODO add X-message header
                    alert('Could not edit task');
                } else {
                    data.finished = finish;
                    jQuery.ajax({
                        type: 'PUT',
                        url: de.sd.todos.tasksUrl+id,
                        data: JSON.stringify(data),
                        contentType: 'application/json',
                        error: function() {
                            alert('Could not edit task.');
                        },
                        success: function(data, status, jqXHR) {
                            if (Math.floor(jqXHR.status / 100) != 2) {
                                // TODO add X-message header
                                alert('Could not edit task');
                            } else if (de.sd.todos.filterQuery) {
                                jQuery('input#filterTasks_filter').val(de.sd.todos.filterQuery);
                                jQuery('form#filterTasks').trigger('submit');
                            } else {
                                de.sd.todos.loadTasks();
                            }
                        }
                    });
                }
            }
        });
    },

    showTasks: function(tasks) {
        jQuery('div#loading').hide();
        var $tasks = jQuery('div#tasks').empty();

        for (var i in tasks) {
            var content = '<div class="task'+(tasks[i].finished ? ' finished' : '')+'"><span class="taskContent">'+tasks[i].name;
            for (var j in tasks[i].contexts) {
                content += '<span class="context">@'+tasks[i].contexts[j]+'</span> ';
            }
            content += '</span>'
                +'<a class="delete button" href="#" data-id="'+tasks[i].id+'" data-name="'+tasks[i].name+'">Delete</a>'
                +'<a class="'+(tasks[i].finished ? 'reactivate' : 'finish')+' button" href="#" data-id="'+tasks[i].id+'">'
                +(tasks[i].finished ? 'Reactivate' : 'Done')+'</a>'
                +'<a class="edit button" href="#" data-id="'+tasks[i].id+'">Edit</a>'
                +'</div>';

            $tasks.append(content);
        }
    },

    extractValues: function(query) {
        var contextMatches = query.match(/@[a-z]+/g) || [],
        object = {
            name : contextMatches.length ? query.substring(0, query.indexOf("@")) : query,
            contexts : []
        };
        // remove @ occurrences
        for (var i in contextMatches) {
            object.contexts.push(contextMatches[i].replace('@',''));
        }
        return object;
    },

    joinContexts: function(contexts) {
        var contextString = "[";
        for (var i in contexts) {
            contextString += '"'+contexts[i]+'",';
        }
        if (contexts.length) {
            contextString = contextString.substring(0, contextString.length - 1);
        }
        contextString += "]";
        return contextString;
    }
};

jQuery(document).ready(de.sd.todos.init());