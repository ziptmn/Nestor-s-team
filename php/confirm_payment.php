<!-- confirm_payment.php -->
<?php
require_once('db.php');

// Проверка наличия параметра id в GET запросе
if (isset($_GET['id'])) {
    $id = intval($_GET['id']);
    $sql = "UPDATE `bank_table` SET `Confirmed?` = 'yes' WHERE `id` = $id";
    if (mysqli_query($conn, $sql)) {
        // Вывод успешного сообщения
        echo "Платеж подтвержден";
    } else {
        // Вывод сообщения об ошибке
        echo "Ошибка: " . mysqli_error($conn);
    }
}
?>