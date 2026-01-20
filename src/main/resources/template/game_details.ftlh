<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Детали игры</title>
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
    <div class="game-details card">
        <div class="game-details-image">
            <#if (game.gameImage)??>
                <img src="/boardgames/uploads/${game.gameImage}" alt="${(game.title)!''}">
            <#else>
                <div class="game-placeholder-big">Нет фото</div>
            </#if>
        </div>

        <div class="game-details-info">
            <h1>${(game.title)!''}</h1>

            <p><strong>Когда:</strong> ${game.eventTime!""}</p>

            <p><strong>Адрес:</strong> ${(game.address)!''}</p>

            <p><strong>Описание:</strong> ${(game.description)!''}</p>

            <p><strong>Категории:</strong>
                <#if (categories)?? && categories?has_content>
                    <#list categories as c>
                        <span class="badge">${c.name}</span>
                    </#list>
                <#else>
                    нет категорий
                </#if>
            </p>

            <p><strong>Игроков:</strong> ${(bookedCount)!0}/${(game.maxPlayers)!4}</p>

            <div class="progress-bar">
                <div class="progress-bar-fill"
                     style="width: ${((bookedCount)!0 * 100 / (game.maxPlayers)!4)?c}%"></div>
            </div>

            <form action="/boardgames/bookingscreate" method="post">
                <input type="hidden" name="gameId" value="${(game.id)!}">
                <input type="hidden" name="playersCount" value="1">
                <button type="submit" class="btn">Записаться</button>
            </form>
        </div>
    </div>
</main>

</body>
</html>
