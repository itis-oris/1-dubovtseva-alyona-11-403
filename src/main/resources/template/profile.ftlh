<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Профиль</title>
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

    <h1>Профиль</h1>

    <div class="card">
        <p><strong>Логин:</strong> ${user.username}</p>
        <p><strong>Имя:</strong> ${user.name}</p>
        <p><strong>Email:</strong> ${user.email}</p>
        <p><strong>Возраст:</strong> ${user.age}</p>
    </div>

    <h2>Мои записи</h2>
    <div class="card">
        <#list bookings as b>
            <p>
                ${b.game.title}
                (<a href="/boardgames/gamesview?id=${b.gameId}">открыть</a>)
                —
            <form action="/boardgames/bookingscancel" method="post" style="display:inline">
                <input type="hidden" name="bookingId" value="${b.id}">
                <button type="submit" class="btn">Отменить</button>
            </form>
            </p>
        </#list>
        <#if bookings?size == 0>
            <p>Вы ещё не записаны ни на одну игру.</p>
        </#if>
    </div>

    <h2>Созданные мной игры</h2>
    <div class="card">
        <#list createdGames as g>
            <p>
                ${g.title}
                (<a href="/boardgames/gamesview?id=${g.id}">открыть</a>)
                —
                <a href="/boardgames/gamesdeleteedit?id=${g.id}">Редактировать</a>
                |
            <form action="/boardgames/gamesdeleteedit" method="post" style="display:inline">
                <input type="hidden" name="id" value="${g.id}">
                <input type="hidden" name="action" value="delete">   <!-- ДОБАВИТЬ -->
                <button type="submit" class="btn">Удалить</button>
            </form>

            </p>
        </#list>
        <#if createdGames?size == 0>
            <p>Вы ещё не создали ни одной игры.</p>
        </#if>
    </div>

</main>

</body>
</html>
