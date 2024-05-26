<nav class="navbar navbar-expand-lg bg-body-tertiary">
        <div class="container-fluid">
        <a class="navbar-brand" href="#">< N / T ></a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link active" aria-current="page" href="index.php">Главная</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="lk.php">Личный кабинет компании</a>
            </li>
            <li class="nav-item">
                <a class="nav-link disabled" aria-disabled="true">Версия 0.1</a>
            </li>
            </ul>
        </div>
        </div>





<?php
if (isset($_SESSION['authorization']) && $_SESSION['authorization'] == true) {
    // Пользователь авторизован
    if (isset($_SESSION['role'])) {
        // Получение роли пользователя из сессии
        $userRole = $_SESSION['role'];

        // В зависимости от роли пользователя выводим соответствующий хедер
        if ($userRole == 1) {
            // Не подтверждённая компания
            echo '';
            // ВЫХОД ДЛЯ АВТОРИЗОВАННЫХ
            echo '<a class="btn btn-danger" href="EXIT.php" role="button"> <i class="fa-solid fa-right-from-bracket" style="color: #ffffff;"></i></a>';
            // Конец NAVBAR:        
            echo '&nbsp;&nbsp;</nav>';
            // Ярлык (Не подтверждённая компания)
            echo '<span class="badge text-bg-secondary" style="padding: 5px; margin: 5px;">Не подтверждённая компания <i class="fa-solid fa-binoculars" style="color: #ffffff;"></i></span>';
        } elseif ($userRole == 2) {
          // Подтверждённая компания
          echo '';
          // ВЫХОД ДЛЯ АВТОРИЗОВАННЫХ
          echo '<a class="btn btn-danger" href="EXIT.php" role="button"> <i class="fa-solid fa-right-from-bracket" style="color: #ffffff;"></i></a>';
          // Конец NAVBAR:        
          echo '&nbsp;&nbsp;</nav>';
          // Ярлык (Подтверждённая компания)
            echo '<span class="badge text-bg-secondary" style="padding: 5px; margin: 5px;">Подтверждённая компания <i class="fa-solid fa-medal" style="color: #ffffff;"></i></span>';
        } elseif ($userRole == 3) {
          // Админ
          echo '';
          // ВЫХОД ДЛЯ АВТОРИЗОВАННЫХ
          echo '<a class="btn btn-danger" href="EXIT.php" role="button"> <i class="fa-solid fa-right-from-bracket" style="color: #ffffff;"></i></a>';
          // Конец NAVBAR:        
          echo '&nbsp;&nbsp;</nav>';
          // Ярлык (Админ)
          echo '<span class="badge text-bg-secondary" style="padding: 5px; margin: 5px;">Админ <i class="fa-solid fa-medal" style="color: #ffffff;"></i></span>';
        }else {
            // Другие роли (role = null)
            echo '';
            echo '<span class="badge text-bg-secondary" style="padding: 5px; margin: 5px;">Кто ты такой? <i class="fa-solid fa-person-hiking" style="color: #ffffff;"></i></span>';
          }
    } else {
        // Роль NULL не установлена в сессии НОВИЧЕК
        echo '';
        // ВЫХОД ДЛЯ АВТОРИЗОВАННЫХ
        echo '<a class="btn btn-danger" href="EXIT.php" role="button"><i class="fa-solid fa-right-from-bracket" style="color: #ffffff;"></i></a>';
        // Конец NAVBAR:        
        echo '&nbsp;&nbsp;</nav>';
        // Ярлык (Новичок)
        echo '<span class="badge text-bg-secondary" style="padding: 5px; margin: 5px;">Новичок <i class="fa-solid fa-person" style="color: #ffffff;"></i></span>';
    }
} else {
    // Пользователь не авторизован (ГОСТЬ)
    echo '<form class="d-flex" role="search">
    </form>';
    // Конец NAVBAR:        
    echo '</nav>';
    // (Гость)
    echo '<span class="badge text-bg-secondary" style="padding: 5px; margin: 5px;">Гость <i class="fa-solid fa-person-circle-question" style="color: #ffffff;"></i></span>';
}
?>