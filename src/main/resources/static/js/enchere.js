async function init() {
    console.log('init chargé ')
    const response = await fetch('/api/creditUser');
    const credit = await response.json();
    verificationInputPrixEnchere(credit);
    afficherTimer();
}



function verificationInputPrixEnchere(credit) {
    console.log('je suis dans verification input prix enchere');
    const inputMontantEnchere = document.getElementById("montantEnchere");
    let prixVente = parseFloat(inputMontantEnchere.min);
    let boutonSubmit = document.getElementById("submitBtn");
    let indicationMessage = document.getElementById("indicationMessage");
    inputMontantEnchere.addEventListener('input', () => {
        let valeurUser = parseFloat(inputMontantEnchere.value);
        const creditRestant = credit - valeurUser;
        if (valeurUser < prixVente || creditRestant < 0) {
            boutonSubmit.disabled = true;
            indicationMessage.classList.remove('hidden');
            if(creditRestant < 0){
                indicationMessage.innerText="Enchère impossible car vous n'avez pas assez de crédit";

            } else {
            indicationMessage.innerText="Le montant saisi doit être supérieur au prix de vente actuel";
        }
        } else {
            indicationMessage.classList.add('hidden');
            boutonSubmit.disabled = false;
        }
    });
}
function afficherTimer(){
    const el = document.getElementById('timer');
    const bidButton = document.querySelector('form button[type="submit"]');

    if (el) {
        const end = new Date(el.dataset.timer);

        // On fixe la hauteur du conteneur pour éviter le saut
        el.style.minHeight = el.offsetHeight + "px";

        const skeleton = el.querySelector('.skeleton');

        const interval = setInterval(() => {
            const now = new Date();
            const diff = end - now;

            if (diff <= 0) {
                el.textContent = "Terminé";
                clearInterval(interval);
                if (bidButton) bidButton.disabled = true; // désactive le bouton
                return;
            }

            const totalSeconds = Math.floor(diff / 1000);
            const d = Math.floor(totalSeconds / 86400); // jours
            const h = Math.floor((totalSeconds % 86400) / 3600); // heures
            const m = Math.floor((totalSeconds % 3600) / 60); // minutes
            const s = totalSeconds % 60; // secondes

            // Supprime le skeleton avec un fade-out
            if (skeleton && skeleton.style.opacity !== '0') {
                skeleton.style.transition = 'opacity 0.3s ease';
                skeleton.style.opacity = '0';
                setTimeout(() => skeleton.remove(), 300);
            }

            let timeStr = "";
            if (d > 0) timeStr += `${d}j `;
            timeStr += `${h}h ${m}m ${s}s`;

            el.textContent = timeStr;

        }, 1000);
    }
}

document.addEventListener("DOMContentLoaded", init);