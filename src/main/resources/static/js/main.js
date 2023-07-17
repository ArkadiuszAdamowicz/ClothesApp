const toggleBtn = document.querySelector('.toggle_btn')
const toggleBtnIcon = document.querySelector('.toggle_btn i')
const dropDownMenu = document.querySelector('.dropdown_menu')
const footerYear = document.querySelector('.footer__year')
const cookieBox = document.querySelector('.cookie-box')
const cookieBtn = document.querySelector('.cookie-btn')
const navs = document.querySelector('.nav');
let isOpen = dropDownMenu.classList.contains('open');
const menuItems=document.querySelectorAll('a')
const scrollSpySections = document.querySelectorAll('.section')

toggleBtn.onclick = function (e) {
    e.stopPropagation();
    dropDownMenu.classList.toggle('open');
    isOpen = !isOpen;

    toggleBtnIcon.classList = isOpen
        ? 'fa-solid fa-xmark'
        : 'fa-solid fa-bars';
};

navs.onclick = function () {
    dropDownMenu.classList.remove('open');
    toggleBtnIcon.classList = 'fa-solid fa-bars';
    isOpen = false;
};

document.addEventListener('click', () => {
    if (dropDownMenu.classList.contains('open')) {
        dropDownMenu.classList.remove('open');
        toggleBtnIcon.classList = 'fa-solid fa-bars';
        isOpen = false;
    }
});

const handleCurrentYear = () => {
    const year = new Date().getFullYear()
    footerYear.innerText = year
}

handleCurrentYear()


const showCookie = () => {
    const cookieEaten = localStorage.getItem('cookie')

    if (cookieEaten) {
        cookieBox.classList.add('hide-cookies')
    }
}

const handleCookieBox = () => {
    localStorage.setItem('cookie', 'true')
    cookieBox.classList.add('hide-cookies')
}

cookieBtn.addEventListener('click', handleCookieBox)
showCookie()


document.getElementById('file').onchange = function () {
    document.getElementById('file-name').textContent = this.value.split('\\').pop();
}

