<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Создание игры</title>
    <link rel="stylesheet" href="/boardgames/static/css/style.css">
</head>
<body>

<header class="header">
    <div class="container header-inner">
        <a href="/boardgames/games" class="logo">BoardGames</a>
        <nav class="nav">
            <a href="/boardgames/games">Игры</a>
            <a href="/boardgames/profile">Профиль</a>
            <a href="/boardgames/logout">Выйти</a>
        </nav>
    </div>
</header>

<main class="container">

    <h1>Создать игру</h1>

    <#if error??>
        <div class="error">${error}</div>
    </#if>

    <form action="/boardgames/creategame"
          method="post"
          enctype="multipart/form-data"
          class="form-card">

        <div class="form-group">
            <label>Название</label>
            <input type="text" name="title" required>
        </div>

        <div class="form-group">
            <label>Адрес</label>
            <input type="text" name="address" required>
        </div>

        <div class="form-group">
            <label>Описание</label>
            <textarea name="description"></textarea>
        </div>

        <div class="form-group">
            <label>Минимум игроков</label>
            <input type="number" name="minPlayers" value="1" min="1">
        </div>

        <div class="form-group">
            <label>Максимум игроков</label>
            <input type="number" name="maxPlayers" value="4" min="1">
        </div>

        <div class="form-group">
            <label>Дата проведения</label>
            <input type="date" name="eventDate" required>
        </div>

        <div class="form-group">
            <label>Время проведения</label>
            <input type="time" name="eventTime" required>
        </div>

        <div class="form-group">
            <label>Категории</label>

            <div id="category-list">
                <#list categories as c>
                    <label style="display:block; margin-bottom:4px;">
                        <input type="checkbox" name="categories" value="${c.id}">
                        ${c.name}
                    </label>
                </#list>
            </div>

            <div style="margin-top:10px;">
                <input type="text" id="new-category" placeholder="Новая категория">
                <button type="button" id="add-category-btn" class="btn">
                    Добавить
                </button>
            </div>

            <p id="category-msg" style="margin-top:5px; font-size:14px;"></p>
        </div>

        <div class="form-group">
            <label>Изображение (необязательно)</label>
            <input type="file" name="gameImage">
        </div>

        <button type="submit" class="btn">Создать</button>
    </form>

</main>


<script src="/boardgames/static/js/category_add.js"></script>

</body>
</html>
