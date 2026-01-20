<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Регистрация</title>
    <link rel="stylesheet" href="/boardgames/static/css/style.css">
</head>
<body>

<header class="header">
    <div class="container header-inner">
        <a href="/boardgames/games" class="logo">BoardGames</a>
        <nav class="nav">
            <a href="/boardgames/login">Войти</a>
        </nav>
    </div>
</header>

<main class="container">
    <div class="form-wrapper">
        <div class="card">
            <h2>Регистрация</h2>

            <#if error??>
                <div class="error">${error}</div>
            </#if>

            <form action="/boardgames/register" method="post" class="form-card">
                <div class="form-group">
                    <label>Логин</label>
                    <input type="text" name="username" value="${(formUser.username)!''}" required>
                </div>

                <div class="form-group">
                    <label>Имя</label>
                    <input type="text" name="name" value="${(formUser.name)!''}" required>
                </div>

                <div class="form-group">
                    <label>Возраст</label>
                    <input type="number" name="age" value="${(formUser.age)!''}" required>
                </div>

                <div class="form-group">
                    <label>Email</label>
                    <input type="email" name="email" value="${(formUser.email)!''}" required>
                </div>

                <div class="form-group">
                    <label>Пароль</label>
                    <input type="password" name="password" required>
                </div>

                <button type="submit">Создать аккаунт</button>
            </form>
        </div>
    </div>
</main>

</body>
</html>