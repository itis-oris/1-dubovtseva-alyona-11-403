<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Запись отменена</title>
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
        <h1>Запись успешно отменена</h1>

        <p>Игра: <strong>${gameTitle!''}</strong></p>
        <p>Дата: <strong>${eventTime!''}</strong></p>

        <a href="/boardgames/profile" class="btn">Вернуться в профиль</a>
        <a href="/boardgames/games" class="btn">На главную</a>
    </div>
</main>

</body>
</html>
