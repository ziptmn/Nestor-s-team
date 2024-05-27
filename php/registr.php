<?php
session_start();
require_once('db.php');

$pass = $_POST['pass'];
$repeatpass = $_POST['repeatpass'];
$email = $_POST['email'];

if (empty($pass) || empty($repeatpass) || empty($email)) {
    $_SESSION['message'] = 'Заполните все поля';
    $_SESSION['message_type'] = 'danger'; // Добавляем тип сообщения для НЕУСПЕШНЫХ
    header('Location: index.php#registration_area');
} else {
    if ($pass != $repeatpass) {
        $_SESSION['message'] = 'Пароли не совпадают';
        $_SESSION['message_type'] = 'danger'; // Добавляем тип сообщения для НЕУСПЕШНЫХ
        header('Location: index.php#registration_area');
    } else {
        $checkEmailQuery = "SELECT * FROM `users_table` WHERE Email = '$email'";
        $result = $conn->query($checkEmailQuery);

        if ($result->num_rows > 0) {
            $_SESSION['message'] = 'Аккаунт с таким email уже существует';
            $_SESSION['message_type'] = 'danger'; // Добавляем тип сообщения для НЕУСПЕШНЫХ
            header('Location: index.php#registration_area');
        } else {
            $sql = "INSERT INTO `users_table` (Email, Pass, role) VALUES ('$email', '$pass', '1')";
            if ($conn->query($sql) === TRUE) {
                $_SESSION['message'] = 'Успешная регистрация! Теперь <a href="index.php" target="_blank">авторизуйтесь</a>.';
                $_SESSION['message_type'] = 'success'; // Добавляем тип сообщения для отличия ошибок от успешной регистрации
                header('Location: index.php#registration_area');
            } else {
                $_SESSION['message'] = 'Ошибка: ' . $conn->error;
                $_SESSION['message_type'] = 'danger'; // Добавляем тип сообщения для НЕУСПЕШНЫХ
                header('Location: index.php#registration_area');
            }
        }
    }
}
