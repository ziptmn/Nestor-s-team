<?php
session_start();
require_once('db.php');

$login = $_POST['login_l'];
$pass = $_POST['pass'];

if (empty($login) || empty($pass))
{
    //echo "Заполните все поля";
    $_SESSION['message'] = 'Заполните все поля';
    header('Location: index.php');
}
else
{
    $sql = "SELECT * FROM `users_table` WHERE email = '$login' AND Pass ='$pass'";
    $result = $conn->query($sql);

    if($result->num_rows > 0)
    {
        while($row = $result->fetch_assoc()){
            //echo "Добро пожаловать " . $row ['email'];
            $_SESSION['message'] = "Добро пожаловать " . $row ['Name'];
            $_SESSION['id'] =  $row ['id'];
            $_SESSION['authorization'] = true;

            if (isset($_SESSION['id'])) {
                $userId = $_SESSION['id'];
            
                $roleQuery = "SELECT role FROM users_table WHERE id = $userId";
                $roleResult = $conn->query($roleQuery);
            
                if ($roleResult && $roleResult->num_rows > 0) {
                    $row = $roleResult->fetch_assoc();
                    $userRole = $row['role'];
            
                    // Сохраняем роль в сессии
                    $_SESSION['role'] = $userRole;
                }
            }



            header('Location: index.php');

        }
    }
    else
    {
        //echo "Нет такого пользователя или пароль неверный";
        $_SESSION['message'] = 'Нет такого пользователя или пароль неверный';
        header('Location: index.php');
    }
}