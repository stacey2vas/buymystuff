function init() {
    const categoriesSelect = document.getElementById('categories');
    if (categoriesSelect) {
        new Choices(categoriesSelect, {
            removeItemButton: true,
            searchEnabled: true,
            shouldSort: false
        });
    }
}

document.addEventListener('DOMContentLoaded', init);