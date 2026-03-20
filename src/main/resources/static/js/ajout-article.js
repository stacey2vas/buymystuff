function init() {
    const categoriesSelect = document.getElementById('categories');
    if (categoriesSelect) {
        new Choices(categoriesSelect, {
            removeItemButton: true,
            searchEnabled: true,
            shouldSort: false
        });
    }
    const fileInput = document.getElementById('imageFile');
    fileInput.addEventListener('change', () => {
        const file = fileInput.files[0];
        if (!file) return;

        const reader = new FileReader();
        reader.onload = e => {
            let imageForm = document.getElementById('imageForm');

            // Si l'image n'existe pas, on la crée
            if (!imageForm) {
                imageForm = document.createElement('img');
                imageForm.id = 'imageForm';
                imageForm.alt = 'Image sélectionnée';
                imageForm.width = 110;
                fileInput.parentNode.querySelector('div.mt-2').appendChild(imageForm);
            }

            // On met à jour le src
            imageForm.src = e.target.result;
        };
        reader.readAsDataURL(file);
    });
    const searchInput = document.getElementById('adresseProprietaire');
    const selectedInput = document.getElementById('adresseSelected');
    const suggestionsList = document.getElementById('suggestionsList');

    let debounceTimeout;

    searchInput.addEventListener('input', () => {
        clearTimeout(debounceTimeout);

        debounceTimeout = setTimeout(async () => {
            const query = searchInput.value.trim();
            if (query.length < 3) {
                suggestionsList.innerHTML = '';
                suggestionsList.classList.add('hidden');
                return;
            }

            const response = await fetch(`https://nominatim.openstreetmap.org/search?q=${encodeURIComponent(query)}&format=json&addressdetails=1`);
            const data = await response.json();

            // Vider l'ancienne liste
            suggestionsList.innerHTML = '';

            if (data.length === 0) {
                suggestionsList.classList.add('hidden');
                return;
            }

            data.forEach(item => {
                const displayValue = `${item.address.road || ''}, ${item.address.postcode || ''}, ${item.address.city || item.address.town || item.address.village || ''}`;

                const li = document.createElement('li');
                li.textContent = displayValue;
                li.className = "px-2 py-1 cursor-pointer hover:bg-[#4934d3]/50";

                li.addEventListener('click', () => {
                    searchInput.value = displayValue;  // Affichage dans input visible
                    selectedInput.value = displayValue; // Valeur finale pour le formulaire
                    suggestionsList.innerHTML = '';
                    suggestionsList.classList.add('hidden');
                });

                suggestionsList.appendChild(li);
            });

            suggestionsList.classList.remove('hidden');
        }, 2000); // debounce 2 secondes
    });
}

document.addEventListener('DOMContentLoaded', init);