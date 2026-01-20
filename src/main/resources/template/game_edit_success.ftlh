<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Изменения сохранены</title>
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
    <div class="card success-card">
        <h1>Изменения успешно сохранены!</h1>

        <p>Вы изменили игру №<strong>${id!}</strong>.</p>

        <p>
            <a href="/boardgames/profile" class="btn">В профиль</a>
            <a href="/boardgames/gamesview?id=${id!}" class="btn">Перейти к игре</a>
            <a href="/boardgames/games" class="btn">На главную</a>
        </p>
    </div>
</main>

</body>
</html>
