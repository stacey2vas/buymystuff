function init() {
    afficherTimer();
    verificationInputDate();
    verificationInputPrix();
     document.getElementById("filterArticle").addEventListener("submit", submitForm );

    const dateStart = document.getElementById("dateStart");

    if (dateStart) {
        dateStart.addEventListener("change", () => {
            const dateEnd = document.getElementById("dateEnd");
            if (!dateEnd) return;

            // La date fin ne peut pas être avant la date début
            dateEnd.min = dateStart.value;
        });
    }

    // Optionnel : empêcher l'envoi du formulaire si validation échoue
    const form = document.getElementById("filterArticle");
    if (form) {
        form.addEventListener("submit", (e) => {
            if (!validerFormulaire()) {
                e.preventDefault();
            }
        });
    }
}

// Timer dynamique
function afficherTimer() {
    document.querySelectorAll('.timer').forEach(el => {
        const end = new Date(el.dataset.timer);

        const interval = setInterval(() => {
            const diff = end - new Date();

            if (diff <= 0) {
                el.textContent = "Terminé";
                clearInterval(interval);
                return;
            }

            const h = Math.floor(diff / 1000 / 60 / 60);
            const m = Math.floor(diff / 1000 / 60 % 60);
            const s = Math.floor(diff / 1000 % 60);

            // Supprime le skeleton seulement au moment de la première mise à jour
            const skeleton = el.querySelector('.skeleton');
            if (skeleton) skeleton.remove();

            el.textContent = `${h}h ${m}m ${s}s`;
        }, 1000);
    });
}

// Vérification des dates
function verificationInputDate() {
    const dateStart = document.getElementById("dateStart");
    const dateEnd = document.getElementById("dateEnd");
    if (!dateStart || !dateEnd) return;

    const today = new Date().toISOString().split("T")[0];

    dateStart.min = today;
    dateEnd.min = today;
}

// Vérification des prix
function verificationInputPrix() {
    const prixMin = document.getElementById("prixMin");
    const prixMax = document.getElementById("prixMax");

    if (!prixMin || !prixMax) return;

    prixMin.addEventListener("input", function() {
        if (this.value !== "") {
            prixMax.min = this.value;
        }
    });
}

// Validation complète du formulaire
function validerFormulaire() {
    const prixMin = parseFloat(document.getElementById("prixMin")?.value || 0);
    const prixMax = parseFloat(document.getElementById("prixMax")?.value || 0);
    const dateStart = document.getElementById("dateStart")?.value;
    const dateEnd = document.getElementById("dateEnd")?.value;

    if (prixMin < 0) {
        alert("Le prix minimum doit être supérieur ou égal à 0");
        return false;
    }

    if (prixMax && prixMax < prixMin) {
        alert("Le prix maximum doit être supérieur ou égal au prix minimum");
        return false;
    }

    if (dateStart && dateEnd && dateEnd < dateStart) {
        alert("La date de fin doit être supérieure ou égale à la date de début");
        return false;
    }

    return true;
}
function submitForm(event){
    event.preventDefault();
    console.log('coucou');
    const filter = {
        nomArticle: document.getElementById("nomArticle").value,
        categorie: document.getElementById("categorie").value,
        prixMin: document.getElementById("prixMin").value,
        prixMax: document.getElementById("prixMax").value,
        dateStart: document.getElementById("dateStart").value,
        dateEnd: document.getElementById("dateEnd").value,
    };
    console.log(filter);
    // fetch("/api/films", {
    //     method: "POST",
    //     headers: {
    //         "Content-Type": "application/json"
    //     },
    //     body: JSON.stringify(filter),
    //     credentials: "same-origin" // 👈 important pour inclure le cookie de session
    // })
    //     .then(r => r.text())
    //     .then(html => {
    //         const container = document.getElementById("filmsContainer");
    //         const tempDiv = document.createElement("div");
    //         tempDiv.innerHTML = html;       // html = uniquement les <div class="relative bg-card ...">
    //         container.replaceChildren(...tempDiv.children);        })
    //     .catch(error => console.error("Erreur :", error));
}
document.addEventListener('DOMContentLoaded', init);