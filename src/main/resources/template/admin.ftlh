<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Админка</title>
    <link rel="stylesheet" href="/boardgames/static/css/style.css">
</head>
<body>

<header class="header">
    <div class="container header-inner">
        <a href="/boardgames/games" class="logo">BoardGames</a>
        <nav class="nav">
            <a href="/boardgames/games">Игры</a>
            <a href="/boardgames/profile">Профиль</a>
            <a href="/boardgames/admin">Админка</a>
            <a href="/boardgames/logout">Выйти</a>
        </nav>
    </div>
</header>

<main class="container">

    <h1>Админ-панель</h1>

    <h2>Категории</h2>
    <div class="card">
        <form action="/boardgames/admin" method="post" class="form-inline">
            <input type="hidden" name="action" value="createCategory">
            <input type="text" name="name" placeholder="Новая категория" required>
            <button type="submit" class="btn">Добавить</button>
        </form>

        <ul>
            <#list categories as c>
                <li>
                    ${c.name}
                    <form action="/boardgames/admin" method="post" style="display:inline">
                        <input type="hidden" name="action" value="deleteCategory">
                        <input type="hidden" name="categoryId" value="${c.id}">
                        <button type="submit" class="btn">Удалить</button>
                    </form>
                </li>
            </#list>
            <#if categories?size == 0>
                <li>Категорий пока нет.</li>
            </#if>
        </ul>
    </div>

    <h2>Пользователи</h2>
    <div class="card">
        <ul>
            <#list users as u>
                <li>
                    ${u.username} (${u.role?string})
                    <#-- не удаляем самого себя -->
                    <#if u.id != currentUser.id>
                        <form action="/boardgames/admin" method="post" style="display:inline">
                            <input type="hidden" name="action" value="deleteUser">
                            <input type="hidden" name="userId" value="${u.id}">
                            <button type="submit" class="btn">Удалить</button>
                        </form>
                    </#if>
                </li>
            </#list>
        </ul>
    </div>

</main>

</body>
</html>
