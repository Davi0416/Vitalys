const btn = document.getElementById('theme-toggle');
const themeIcon = document.getElementById('theme-icon');
const html = document.documentElement;

const themes = [
    { 
        name: 'light',
        svg: `<circle cx="12" cy="12" r="4"/><line x1="12" y1="2" x2="12" y2="5"/><line x1="12" y1="19" x2="12" y2="22"/><line x1="2" y1="12" x2="5" y2="12"/><line x1="19" y1="12" x2="22" y2="12"/><line x1="4.93" y1="4.93" x2="7.05" y2="7.05"/><line x1="16.95" y1="16.95" x2="19.07" y2="19.07"/><line x1="4.93" y1="19.07" x2="7.05" y2="16.95"/><line x1="16.95" y1="7.05" x2="19.07" y2="4.93"/>`
    },
    { 
        name: 'system',
        svg: `<circle cx="12" cy="12" r="9"/><path d="M12 3v18"/><path d="M12 3a9 9 0 0 1 0 18z" fill="currentColor" stroke="none" opacity="0.4"/>`
    },
    { 
        name: 'dark',
        svg: `<path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"/>`
    }
];

let currentIndex = 0;

// Aplica o tema salvo ao carregar — se não houver nada salvo, fica no light (índice 0)
const saved = localStorage.getItem('vitalys-theme-preference');
if (saved) {
    const savedIndex = themes.findIndex(t => t.name === saved);
    if (savedIndex !== -1) currentIndex = savedIndex;
}
applyTheme(themes[currentIndex]);

btn.addEventListener('click', () => {
    currentIndex = (currentIndex + 1) % themes.length;
    applyTheme(themes[currentIndex]);
});

function applyTheme(theme) {
    if (theme.name === 'system') {
        html.removeAttribute('data-theme');
    } else {
        html.setAttribute('data-theme', theme.name);
    }
    themeIcon.innerHTML = theme.svg;
    localStorage.setItem('vitalys-theme-preference', theme.name);
}


