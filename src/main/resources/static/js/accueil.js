let statut = null;
let selectValue = null;

function init() {
    console.log('init chargé ')
    afficherTimer();
    verificationInputDate();
    verificationInputPrix();
    verificationsButtonsSearch();
    disabledButtonsToSelectValue();
    afficherModal();
    document.getElementById("filterArticle").addEventListener("submit", submitForm);
    const formModal = document.querySelector("#modalEnchere form");
    formModal.addEventListener("submit", submitEnchereModal);}

// Timer dynamique
function afficherTimer() {
    document.querySelectorAll('.timer').forEach(element => {
        const start = new Date(element.dataset.timerDebut);
        const end = new Date(element.dataset.timerEnd);

        const interval = setInterval(() => {
            const now = new Date();

            let diff;

            if (now < start) {
                // Avant début → countdown vers dateDebut
                diff = start - now;
            } else if (now >= start && now < end) {
                // Pendant → countdown vers dateFin
                diff = end - now;
            } else {
                // Terminé
                element.textContent = "Terminé";
                clearInterval(interval);
                return;
            }

            const totalSeconds = Math.floor(diff / 1000);
            const d = Math.floor(totalSeconds / 86400);
            const h = Math.floor((totalSeconds % 86400) / 3600);
            const m = Math.floor((totalSeconds % 3600) / 60);
            const s = totalSeconds % 60;

            // Supprime le skeleton une seule fois
            const skeleton = element.querySelector('.skeleton');
            if (skeleton) skeleton.remove();

            let timeStr = "";
            if (d > 0) timeStr += `${d}j `;
            timeStr += `${h}h ${m}m ${s}s`;

            element.textContent = timeStr;

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

    prixMin.addEventListener("input", function () {
        if (this.value !== "") {
            prixMax.min = this.value;
        }
    });
}


function submitForm(event) {
    event.preventDefault();
    if (!validerFormulaire()) {
        return;
    }
    const filter = {
        nomArticle: document.getElementById("nomArticle").value,
        categorie: document.getElementById("categorie").value,
        prixMin: document.getElementById("prixMin").value || null,
        prixMax: document.getElementById("prixMax").value || null,
        statut: statut,
        selectValue: selectValue
    };
    fetch("/api/articles", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(filter),
        credentials: "same-origin"
    })
        .then(r => r.text())
        .then(html => {
            const container = document.getElementById("articlesContainer");
            const tempDiv = document.createElement("div");
            tempDiv.innerHTML = html;

            container.replaceChildren(...tempDiv.children);
            afficherTimer();
        })
        .catch(error => console.error("Erreur :", error));
}

// Validation complète du formulaire
function validerFormulaire() {
    const prixMin = parseFloat(document.getElementById("prixMin")?.value || 0);
    const prixMax = parseFloat(document.getElementById("prixMax")?.value || 0);

    if (prixMin < 0) {
        alert("Le prix minimum doit être supérieur ou égal à 0");
        return false;
    }

    if (prixMax && prixMax < prixMin) {
        alert("Le prix maximum doit être supérieur ou égal au prix minimum");
        return false;
    }

    return true;
}

function verificationsButtonsSearch() {
    const buttons = document.querySelectorAll('.toggle-btn');
    buttons.forEach(btn => {
        btn.addEventListener('click', () => {
            const value = btn.dataset.value;
            const isSelected = btn.classList.contains("bg-[#4934d3]");

            if (isSelected) {
                btn.classList.remove('bg-[#4934d3]');
                statut = null;
                return;
            }

            // reset visuel
            buttons.forEach(b => b.classList.remove('bg-[#4934d3]'));
            // active bouton
            btn.classList.add('bg-[#4934d3]');
            // stocke dans ton state
            statut = value;

        });


    });
}

function disabledButtonsToSelectValue() {
    const select = document.querySelector("select[name='vue']");
    if (select) {
        const buttons = document.querySelectorAll(".toggle-btn");

        select.addEventListener("change", () => {
            const value = select.value;

            // reset visuel + activation
            buttons.forEach(btn => {
                btn.disabled = false;
                btn.classList.remove("opacity-50", "cursor-not-allowed");
            });

            // reset statut global si besoin
            statut = null;
            buttons.forEach(b => b.classList.remove('bg-[#4934d3]'));

            // 🔥 LOGIQUE
            if (value === "gagnees") {
                buttons.forEach(btn => {
                    if (btn.dataset.value !== "terminee") {
                        disableButton(btn);
                    }
                });

                // auto sélection "terminée"
                const btnTerminee = document.querySelector('[data-value="terminee"]');
                btnTerminee.classList.add('bg-[#4934d3]');
                statut = "terminee";
            }

            if (value === "participation") {
                buttons.forEach(btn => {
                    if (btn.dataset.value === "pascommencee") {
                        disableButton(btn);
                    }
                });
            }
            if (value === "mesVentes") {
                // tout autorisé → rien à faire
            }
            if (value === "toutes") {
                selectValue = null;
            } else {
                selectValue = value;
            }
        });
    }
}

// helper propre
function disableButton(btn) {
    btn.disabled = true;
    btn.classList.add("opacity-50", "cursor-not-allowed");
}

function afficherModal() {
    const modal = document.getElementById("modalEnchere");
    const closeBtn = document.getElementById("closeModal");
    const articleIdInput = modal.querySelector("#articleId");
    const articleTitleSpan = modal.querySelector("#articleTitle");
    const prixMinP = modal.querySelector("#prixMin");
    const buttons = document.querySelectorAll(".btnModal");
    const modalContent = modal.querySelector("div.relative"); // Le contenu de la modale

    modal.addEventListener('click', () => {
        modal.classList.add('hidden');
    })
    // Empêcher la fermeture quand on clique sur le contenu de la modale
    modalContent.addEventListener('click', (event) => {
        event.stopPropagation();
    });
    // Fermer la modale
    closeBtn.addEventListener('click', () => {
        modal.classList.add('hidden');
    });
    // Ouvrir la modale avec les infos de l'article
    buttons.forEach(btn => {
        btn.addEventListener('click', () => {
            const articleId = btn.dataset.id;
            const articleTitle = btn.dataset.title;
            const articlePrice = parseInt(btn.dataset.price);
            const articlePriceInitial = parseInt(btn.dataset.priceInitial);
            const montantInput = modal.querySelector("#montantEnchere");

            let minEnchere = (articlePrice !== 0) ? articlePrice + 1 : articlePriceInitial + 1;
            // Remplir les champs dans la modale
            articleIdInput.value = articleId;
            articleTitleSpan.textContent = articleTitle;
            prixMinP.textContent = "Enchère minimale : " + minEnchere + " €";

            montantInput.min = minEnchere;

            montantInput.value = minEnchere;            // Afficher la modale
            modal.classList.remove('hidden');

            console.log("Article:", articleId, articleTitle, articlePrice);
        });
    });
}
function submitEnchereModal(event){
    event.preventDefault();
    let valueEnchere = document.getElementById('montantEnchere').value;
    let idArticle = document.getElementById('articleId').value;
    console.log('Les données : ' + valueEnchere + ' et : ' + idArticle);
}
document.addEventListener("DOMContentLoaded", init);