$(document).ready(function () {
    /*getAllCategories();*/
    setUserName();
    reloadItems();
});
function addItem() {
    $.ajax({
        type: 'POST',
        url: 'http://localhost:8081/job4j_todo/items',
        data: JSON.stringify({
            description: $('#description').val()
        }),
        dataType: 'json'
    }).done(function (data) {
        addRow(data);
    }).fail(function (err) {
        console.log(err);
    });
}
    function addRow(item) {
        let id = item.id;
        let description = item.description;
        var user = item.user.name;
        let created = item.created;
        const checkBoxId = 'check_' + id;
        let done = item.done == true ? 'checked' : '';
        $('#itemsList tr:last').after(
            '<tr>' +
            '<td>' + id + '</td>' +
            '<td>' + description + '</td>' +
            '<td>' + created + '</td>' +
            '<td>' + user + '</td>' +
            '<td>' + '<input type=\"checkbox\" id=' + checkBoxId + ' ' + done + '>' + '</td>' +
            '</tr>');
        let sel = document.getElementById(checkBoxId);
        sel.addEventListener('click', checkDone, false);
    }

    function checkDone() {
        let id = this.id.split('_')[1];
        let checked = this.checked;
        $.ajax({
            type: 'PUT',
            url: 'http://localhost:8081/job4j_todo/items',
            data: JSON.stringify({
                id: id,
                done: checked,
            }),
            dataType: 'json'
        }).done(function (data) {
            reloadItems();
        }).fail(function (err) {
            console.log(err);
        });
    }
    function reloadItems() {
        let table = document.getElementById('table');
        table.innerHTML = '<tr></tr>';
        let checked = document.getElementById('showAll').checked;
        $.ajax({
            type: 'GET',
            url: 'http://localhost:8081/job4j_todo/items',
            dataType: 'json'
        }).done(function (data) {
            for (let item of data) {
                let done = item.done;
                if (checked || !done) {
                    addRow(item);
                }
            }
        }).fail(function (err) {
            console.log(err);
        });
    }
function setUserName() {
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8081/job4j_todo/user ',
        dataType: 'json'
    }).done(function (response) {
        if (response != undefined) {
            document.getElementById('userOut').innerHTML = response.name + ' | Выйти';
        }
    }).fail(function (err) {
        alert('Пользователь не авторизован!');
        console.log(err);
    });
}