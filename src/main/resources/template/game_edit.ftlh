<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Редактирование</title>
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

    <h1>Редактировать игру</h1>

    <#if error??>
        <div class="error">${error}</div>
    </#if>

    <#if game.eventTime?? && game.eventTime?has_content>
        <p><strong>Текущее время проведения:</strong> ${game.eventTime}</p>
    </#if>

    <form action="/boardgames/gamesdeleteedit" method="post" class="form-card">

        <input type="hidden" name="id" value="${(game.id)!}">

        <div class="form-group">
            <label>Название</label>
            <input type="text" name="title" value="${(game.title)!''}" required>
        </div>

        <div class="form-group">
            <label>Адрес</label>
            <input type="text" name="address" value="${(game.address)!''}" required>
        </div>

        <div class="form-group">
            <label>Описание</label>
            <textarea name="description">${(game.description)!''}</textarea>
        </div>

        <div class="form-group">
            <label>Минимум игроков</label>
            <input type="number" name="minPlayers" value="${(game.minPlayers)!1}" min="1">
        </div>

        <div class="form-group">
            <label>Максимум игроков</label>
            <input type="number" name="maxPlayers" value="${(game.maxPlayers)!4}" min="1">
        </div>

        <div class="form-group">
            <label>Новая дата проведения</label>
            <input type="date" name="eventDate" required>
        </div>

        <div class="form-group">
            <label>Новое время проведения</label>
            <input type="time" name="eventTime" required>
        </div>

        <button type="submit" class="btn">Сохранить</button>
    </form>

    <form action="/boardgames/gamesdeleteedit" method="post" class="mt-2">
        <input type="hidden" name="id" value="${(game.id)!}">
        <input type="hidden" name="action" value="delete">
        <button class="btn btn-danger" onclick="return confirm('Удалить игру?');">Удалить игру</button>
    </form>

</main>

</body>
</html>
