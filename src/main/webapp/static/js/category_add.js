document.getElementById("add-category-btn").addEventListener("click", async () => {
    const input = document.getElementById("new-category");
    const name = input.value.trim();
    const msg = document.getElementById("category-msg");
    const list = document.getElementById("category-list");

    msg.textContent = "";
    msg.className = "";

    if (name.length === 0) {
        msg.textContent = "Введите название";
        msg.className = "error";
        return;
    }

    try {
        const resp = await fetch("/boardgames/categoriesadd", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: "name=" + encodeURIComponent(name)
        });

        const text = await resp.text();

        if (resp.status === 409) {
            msg.textContent = "Категория уже существует";
            msg.className = "error";
            return;
        }

        if (!resp.ok) {
            msg.textContent = "Ошибка: " + text;
            msg.className = "error";
            return;
        }

        const json = JSON.parse(text);

        list.innerHTML += `
            <label style="display:block; margin-bottom:4px;">
                <input type="checkbox" name="categories" value="${json.id}" checked>
                ${json.name}
            </label>
        `;

        input.value = "";
        msg.textContent = "Категория добавлена!";
        msg.className = "success";

    } catch (e) {
        msg.textContent = "Ошибка сети";
        msg.className = "error";
    }
});
