package com.example.logistics.controller;

import com.example.logistics.model.Order;
import com.example.logistics.service.OrderService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.Document;
import java.io.*;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    // Новый маршрут для корневого URL
    @GetMapping("/")
    public String home() {
        return "redirect:/orders"; // Перенаправление на /orders
    }
    
    @GetMapping
    public String getAllOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/new")
    public String createOrderForm(Model model) {
        model.addAttribute("order", new Order());
        return "order_form";
    }

    @PostMapping("/create")
    public String createOrder(@ModelAttribute("order") Order order) {
        orderService.createOrder(order);
        return "redirect:/orders";
    }

    @GetMapping("/{id}/edit")
    public String editOrderForm(@PathVariable Long id, Model model) {
        Order order = orderService.getOrder(id);
        model.addAttribute("order", order);
        return "order_form";
    }

    @PostMapping("/{id}/update")
    public String updateOrder(@PathVariable Long id, @ModelAttribute Order order) {
        order.setId(id);
        orderService.updateOrder(order);
        return "redirect:/orders";
    }

    @GetMapping("/{id}/delete")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return "redirect:/orders";
    }
    // Метод для отображения страницы отслеживания грузов
    @GetMapping("/tracking")
    public String trackingPage() {
        return "tracking"; // Имя шаблона для отслеживания
    }

    // Метод для обработки формы отслеживания
    @PostMapping("/tracking")
    public String trackCargo(@RequestParam Long id, Model model) {
        Order order = orderService.getOrder(id);
        model.addAttribute("order", order);
        return "tracking"; // Возврат на страницу отслеживания с данными заказа
    }

    // Метод для отображения страницы генерации отчетов
    @GetMapping("/reports")
    public String reportPage() {
        return "report"; // Имя шаблона для отчетов
    }

//    @PostMapping("/reports")
//    public String generateReport(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
//            Model model) {
//
//        // Валидация дат
//        if (startDate.isAfter(endDate)) {
//            model.addAttribute("error", "Дата начала не может быть позже даты окончания.");
//            return "report"; // Возврат на страницу отчетов с сообщением об ошибке
//        }
//
//        List<Order> reports = orderService.findOrdersByDateRange(startDate.toString(), endDate.toString());
//        model.addAttribute("reports", reports);
//        model.addAttribute("startDate", startDate);
//        model.addAttribute("endDate", endDate);
//        return "report"; // Возврат на страницу отчетов с данными
//    }
//
//    @GetMapping("/reports/csv")
//    public ResponseEntity<String> exportToCSV(@RequestParam String startDate, @RequestParam String endDate) {
//        List<Order> reports = orderService.findOrdersByDateRange(startDate, endDate);
//        StringBuilder csvBuilder = new StringBuilder();
//        csvBuilder.append("ID,Описание груза,Статус,Дата создания\n");
//        for (Order order : reports) {
//            csvBuilder.append(order.getId()).append(",")
//                    .append(order.getCargoDescription()).append(",")
//                    .append(order.getStatus()).append(",")
//                    .append(order.getCreatedAt()).append("\n");
//        }
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Disposition", "attachment; filename=report.csv");
//        return new ResponseEntity<>(csvBuilder.toString(), headers, HttpStatus.OK);
//    }
//
//    // метод для генерации PDF
//    @GetMapping("/reports/pdf")
//    public ResponseEntity<byte[]> exportToPDF(@RequestParam String startDate, @RequestParam String endDate) {
//        List<Order> reports = orderService.findOrdersByDateRange(startDate, endDate);
//
//        PDDocument document = null;
//        PDPageContentStream contentStream = null;
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//
//        try {
//            document = new PDDocument();
//            PDPage page = new PDPage();
//            document.addPage(page);
//
//            contentStream = new PDPageContentStream(document, page);
//            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
//            contentStream.beginText();
//            contentStream.newLineAtOffset(25, 700);
//            contentStream.showText("Отчет с " + startDate + " по " + endDate);
//            contentStream.endText();
//
//            // Добавление данных в таблицу
//            contentStream.setFont(PDType1Font.HELVETICA, 12);
//            int yPosition = 650;
//            for (Order order : reports) {
//                contentStream.beginText();
//                contentStream.newLineAtOffset(25, yPosition);
//                contentStream.showText(order.getId() + " " + order.getCargoDescription() + " " + order.getStatus() + " " + order.getCreatedAt());
//                contentStream.endText();
//                yPosition -= 15; // Уменьшаем позицию для следующей строки
//            }
//
//            // Сохранение документа в выходной поток
//            document.save(outputStream);
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
//                    .body(outputStream.toByteArray());
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        } finally {
//            // Закрываем ресурсы
//            try {
//                if (contentStream != null) {
//                    contentStream.close();
//                }
//                if (document != null) {
//                    document.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
@PostMapping("/reports")
public String generateReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        Model model) {

    // Валидация дат
    if (startDate.isAfter(endDate)) {
        model.addAttribute("error", "Дата начала не может быть позже даты окончания.");
        return "report"; // Возврат на страницу отчетов с сообщением об ошибке
    }

    List<Order> reports = orderService.findOrdersByDateRange(startDate.toString(), endDate.toString());
    model.addAttribute("reports", reports);
    model.addAttribute("startDate", startDate);
    model.addAttribute("endDate", endDate);
    return "report"; // Возврат на страницу отчетов с данными
}

    @PostMapping("/reports/csv")
    public ResponseEntity<String> exportToCSV(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
                                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) {
        List<Order> reports = orderService.findOrdersByDateRange(startDate, endDate);
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("ID,Описание груза,Статус,Дата создания\n");
        for (Order order : reports) {
            csvBuilder.append(order.getId()).append(",")
                    .append(order.getCargoDescription()).append(",")
                    .append(order.getStatus()).append(",")
                    .append(order.getCreatedAt()).append("\n");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=report.csv");
        return new ResponseEntity<>(csvBuilder.toString(), headers, HttpStatus.OK);
    }

    @PostMapping("/reports/pdf")
    public ResponseEntity<byte[]> exportToPDF(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
                                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) {
        List<Order> reports = orderService.findOrdersByDateRange(startDate, endDate);
        PDDocument document = new PDDocument();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Загрузка шрифта из ресурсов
            InputStream fontStream = getClass().getResourceAsStream("/static/Arial_Unicode_MS.ttf");
            if (fontStream == null) {
                throw new FileNotFoundException("Шрифт не найден в ресурсах: /static/Arial_Unicode_MS.ttf");
            }
            PDTrueTypeFont font = PDTrueTypeFont.loadTTF(document, fontStream);

            contentStream.setFont(font, 16);
            contentStream.beginText();
            contentStream.newLineAtOffset(25, 700);
            contentStream.showText("Отчет с " + startDate + " по " + endDate);
            contentStream.endText();

            // Добавление данных в таблицу
            int yPosition = 650;
            for (Order order : reports) {
                contentStream.beginText();
                contentStream.newLineAtOffset(25, yPosition);
                contentStream.showText(order.getId() + " " + order.getCargoDescription() + " " + order.getStatus() + " " + order.getCreatedAt());
                contentStream.endText();
                yPosition -= 15; // Уменьшаем позицию для следующей строки
            }

            contentStream.close();
            document.save(outputStream);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
                    .body(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(("Ошибка при генерации PDF: " + e.getMessage()).getBytes());
        } finally {
            try {
                document.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
