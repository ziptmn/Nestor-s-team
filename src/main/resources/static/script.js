var token = 'Bearer ';

document.getElementById('login-form').addEventListener('submit', function (event) {
    event.preventDefault();
    const email = document.getElementById('loginEmail').value;
    password = document.getElementById('loginPassword').value;
    sessionStorage.setItem('email', email);

    try {
        fetch('http://localhost:8080/verifier-service/auth/sign-in', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                email: email,
                password: password
            }),
        }).then(response => {
            if (response.ok) {
                window.location.href = 'http://localhost:8080/verifier-service/auth/otp-page';
            }else{
                showMessage('login-message', 'danger', response.message);
            }
        }).catch(error => {
            showMessage('login-message', 'danger', error);
        });
    } catch (error) {
        showMessage('login-message', 'danger', 'Произошла ошибка при отправке формы.');
    }
});
document.getElementById('otp-form').addEventListener('submit', function (event) {
    event.preventDefault();
    const otp = document.getElementById('code').value;
    const email = sessionStorage.getItem('email');
    const fullName = sessionStorage.getItem('fullName');
    const company = sessionStorage.getItem('company');
    console.log(email)
    console.log(otp)
    try {
        if (fullName === null && company === null) {
            fetch('http://localhost:8080/verifier-service/auth/sign-in/otp', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    email: email,
                    otp: otp
                }),
            }).then(response => {
                if (response.ok) {
                    return response.json();
                }else{
                    sessionStorage.setItem('email', 'Bearer '+response);
                    showMessage('login-message', 'danger', response.message);
                }
            }).then(data => {
                const accessToken = data.accessToken;
                sessionStorage.setItem('token', accessToken);
                window.location.href = 'http://localhost:8080/verifier-service/profile-ui';
            }).catch(error => {
                showMessage('login-message', 'danger', error);
            });
        }else{
            fetch('http://localhost:8080/verifier-service/auth/sign-up/otp?code=${code}', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    fullName: fullName,
                    company: company,
                    inn: inn,
                    ogrn: ogrn,
                    position: position,
                    email: email,
                    password: password
                }),
            }).then(response => {
                if (response.ok) {
                    window.location.href = 'http://localhost:8080/verifier-service/profile-ui';
                }else{
                    showMessage('login-message', 'danger', response.message);
                }
            }).catch(error => {
                showMessage('login-message', 'danger', error);
            });
        }
    } catch (error) {
        showMessage('login-message', 'danger', 'Произошла ошибка при отправке формы.');
    }
});

document.getElementById('register-form').addEventListener('submit', async function (event) {
    event.preventDefault();
    fullName = document.getElementById('fullName').value;
    company = document.getElementById('company').value;
    inn = document.getElementById('inn').value;
    ogrn = document.getElementById('ogrn').value;
    position = document.getElementById('position').value;
    email = document.getElementById('registerEmail').value;
    password = document.getElementById('registerPassword').value;
    sessionStorage.setItem('fullName', email);
    sessionStorage.setItem('company', email);
    sessionStorage.setItem('inn', email);
    sessionStorage.setItem('ogrn', email);
    sessionStorage.setItem('position', email);
    sessionStorage.setItem('email', email);

    try {
        const response = await fetch('http://localhost:8080/verifier-service/auth/sign-up', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                fullName: fullName,
                company: company,
                inn: inn,
                ogrn: ogrn,
                position: position,
                email: email,
                password: password
            })
        });

        const result = await response.json();

        if (response.ok) {
            window.location.href = 'http://localhost:8080/verifier-service/otp-page';
        } else {
            showMessage('register-message', 'danger', result.message);
        }
    } catch (error) {
        showMessage('register-message', 'danger', 'Произошла ошибка при отправке формы.');
    }
});

function showMessage(elementId, type, message) {
    const messageElement = document.getElementById(elementId);
    const messageIcon = messageElement.querySelector('#' + elementId + '-icon');
    const messageText = messageElement.querySelector('#' + elementId + '-text');

    messageElement.className = `alert alert-${type} d-flex align-items-center`;
    messageIcon.innerHTML = type === 'success' ? '<i class="fa-solid fa-thumbs-up"></i>' : '<i class="fa-solid fa-circle-xmark"></i>';
    messageText.textContent = message;
    messageElement.style.display = 'flex';
}