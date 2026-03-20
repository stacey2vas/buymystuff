// -------------------- Initialisation --------------------
document.addEventListener('DOMContentLoaded', init);

function init() {
    initChoices();
    initImagePreview();
    initAdresseAutocomplete();
    initFormValidation();
}

// -------------------- Validation complète du formulaire --------------------
function initFormValidation() {

    const nom = document.getElementById('nom');
    const prix = document.getElementById('prixInitial');
    const description = document.getElementById('description');
    const categories = document.getElementById('categories');
    const dateDebut = document.getElementById('dateDebut');
    const dateFin = document.getElementById('dateFin');
    const adresse = document.getElementById('adresseSelected');
    const submitBtn = document.getElementById('submitButton');
    const errorMsg = document.getElementById('errorMsg');

    // Désactivé au départ
    submitBtn.disabled = true;

    function validateForm() {

        // ---------------- Champs ----------------
        const nomValid = nom.value.trim() !== "";
        const prixValid = prix.value.trim() !== "" && Number(prix.value) > 0;
        const descriptionValid = description.value.trim() !== "";
        const categoriesValid = categories.selectedOptions.length > 0;
        const dateDebutValid = dateDebut.value !== "";
        const dateFinValid = dateFin.value !== "";
        const adresseValid = adresse.value.trim() !== "";

        const fieldsValid = nomValid && prixValid && descriptionValid &&
            categoriesValid && dateDebutValid && dateFinValid && adresseValid;

        // ---------------- Dates ----------------
        let datesValid = true;

        if (dateDebutValid && dateFinValid) {
            const debut = new Date(dateDebut.value);
            const fin = new Date(dateFin.value);

            if (fin <= debut) {
                errorMsg.textContent = "La date/heure de fin doit être après la date/heure de début.";
                datesValid = false;
            }
            if(fin > debut){
                errorMsg.textContent = "";
                datesValid = true;
            }
        } else {
            errorMsg.textContent = "";
            datesValid = false;
        }

        // ---------------- Activation bouton ----------------
        submitBtn.disabled = !(fieldsValid && datesValid);
    }

    // ---------------- Listeners ----------------
    [nom, prix, description, categories, dateDebut, dateFin, adresse].forEach(el => {
        el.addEventListener('input', validateForm);
        el.addEventListener('change', validateForm);
    });

    // ---------------- UX dates (min/max) ----------------
    dateDebut.addEventListener('input', () => {
        dateFin.min = dateDebut.value || '';
    });

    dateFin.addEventListener('input', () => {
        dateDebut.max = dateFin.value || '';
    });

    // Vérification initiale (mode édition)
    validateForm();
}

// -------------------- Gestion du select avec Choices.js --------------------
function initChoices() {
    const categoriesSelect = document.getElementById('categories');
    if (!categoriesSelect) return;

    new Choices(categoriesSelect, {
        removeItemButton: true,
        searchEnabled: true,
        shouldSort: false
    });
}

// -------------------- Prévisualisation de l'image --------------------
function initImagePreview() {
    const fileInput = document.getElementById('imageFile');
    if (!fileInput) return;

    fileInput.addEventListener('change', () => {
        const file = fileInput.files[0];
        if (!file) return;

        const reader = new FileReader();
        reader.onload = e => {
            let imageForm = document.getElementById('imageForm');

            if (!imageForm) {
                imageForm = document.createElement('img');
                imageForm.id = 'imageForm';
                imageForm.className = "w-11/12 h-full object-contain";
                const container = document.getElementById('imagePreviewContainer');
                if (container) container.appendChild(imageForm);
            }

            imageForm.src = e.target.result;
        };
        reader.readAsDataURL(file);
    });
}

// -------------------- Autocomplete adresse --------------------
function initAdresseAutocomplete() {
    const searchInput = document.getElementById('adresseProprietaire');
    const selectedInput = document.getElementById('adresseSelected');
    const suggestionsList = document.getElementById('suggestionsList');
    const helpMessage = document.getElementById('adresseHelp');

    if (!searchInput || !selectedInput || !suggestionsList || !helpMessage) return;

    let debounceTimeout;

    searchInput.addEventListener('input', () => {
        clearTimeout(debounceTimeout);

        debounceTimeout = setTimeout(async () => {
            const query = searchInput.value.trim();

            if (/^\d+$/.test(query) || query.length < 2) {
                suggestionsList.innerHTML = '';
                suggestionsList.classList.add('hidden');
                helpMessage.textContent = 'Tapez un nom de rue pour rechercher.';
                helpMessage.classList.remove('hidden');
                selectedInput.value = '';
                return;
            }

            helpMessage.classList.add('hidden');

            try {
                const response = await fetch(`https://nominatim.openstreetmap.org/search?q=${encodeURIComponent(query)}&format=json&addressdetails=1&limit=10`);
                const data = await response.json();

                const streets = data.filter(item => item.address.road);

                suggestionsList.innerHTML = '';

                if (streets.length === 0) {
                    helpMessage.textContent = 'Aucun résultat trouvé pour cette rue.';
                    helpMessage.classList.remove('hidden');
                    suggestionsList.classList.add('hidden');
                    selectedInput.value = '';
                    return;
                }

                streets.forEach(item => {
                    const displayValue = `${item.address.road}, ${item.address.postcode || ''}, ${item.address.city || item.address.town || item.address.village || ''}`;

                    const li = document.createElement('li');
                    li.textContent = displayValue;
                    li.className = "px-2 py-1 cursor-pointer hover:bg-[#4934d3]/50";

                    li.addEventListener('click', () => {
                        searchInput.value = displayValue;
                        selectedInput.value = displayValue;
                        suggestionsList.innerHTML = '';
                        suggestionsList.classList.add('hidden');

                        // 🔥 IMPORTANT → revalider le formulaire
                        selectedInput.dispatchEvent(new Event('input'));
                    });

                    suggestionsList.appendChild(li);
                });

                suggestionsList.classList.remove('hidden');

            } catch (error) {
                console.error('Erreur lors de la recherche d\'adresse :', error);
            }
        }, 500);
    });
}