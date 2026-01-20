<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Вход</title>
    <link rel="stylesheet" href="/boardgames/static/css/style.css">
</head>
<body>

<header class="header">
    <div class="container header-inner">
        <a href="/boardgames/games" class="logo">BoardGames</a>
        <nav class="nav">
            <a href="/boardgames/register">Регистрация</a>
        </nav>
    </div>
</header>

<main class="container">
    <div class="form-wrapper">
        <div class="card">
            <h2>Вход</h2>

            <#if error??>
                <div class="error">${error}</div>
            </#if>

            <form action="/boardgames/login" method="post" class="form-card">
                <div class="form-group">
                    <label>Логин</label>
                    <input type="text" name="username" required>
                </div>

                <div class="form-group">
                    <label>Пароль</label>
                    <input type="password" name="password" required>
                </div>

                <button type="submit">Войти</button>
            </form>

            <p class="text-center mt-2">
                Нет аккаунта?
                <a href="/boardgames/register">Регистрация</a>
            </p>
        </div>
    </div>
</main>

</body>
</html>