<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Игры</title>
    <link rel="stylesheet" href="/boardgames/static/css/style.css">
</head>
<body>

<header class="header">
    <div class="container header-inner">
        <a href="/boardgames/games" class="logo">BoardGames</a>
        <nav class="nav">
            <#if currentUser??>
                <a href="/boardgames/games">Игры</a>
                <a href="/boardgames/profile">Профиль</a>
                <#if currentUser.role?string == "ADMIN">
                    <a href="/boardgames/admin">Админка</a>
                </#if>
                <a href="/boardgames/logout">Выйти (${currentUser.username})</a>
            <#else>
                <a href="/boardgames/login">Войти</a>
                <a href="/boardgames/register">Регистрация</a>
            </#if>
        </nav>
    </div>
</header>

<main class="container">
    <div class="flex-between">
        <h1>Доступные игры</h1>
        <a href="/boardgames/creategame" class="btn">Создать игру</a>
    </div>

    <div class="games-grid">
        <#list games as game>
            <div class="game-card">
                <a href="/boardgames/gamesview?id=${game.id}">
                    <#if game.gameImage??>
                        <img src="/boardgames/uploads/${game.gameImage}" alt="${game.title}">
                    <#else>
                        <div class="no-photo">Нет фото</div>
                    </#if>
                    <div class="game-card-content">
                        <h3>${game.title}</h3>
                        <p class="text-muted">${game.address}</p>
                    </div>
                </a>
            </div>
        </#list>
    </div>

    <#if games?size == 0>
        <p class="text-center">Пока нет игр.</p>
    </#if>
</main>

</body>
</html>