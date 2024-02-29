package com.itmo.coursework.controller;

import com.itmo.coursework.dto.AvailableToReportAnimalsDTO;
import com.itmo.coursework.dto.ReportHistoryParamsDTO;
import com.itmo.coursework.model.Report;
import com.itmo.coursework.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/backend/api/v1/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        return ResponseEntity.ok(reportService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable Long id) {
        return reportService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Report> upsertReport(@RequestBody Report report) {
        return ResponseEntity.ok(reportService.upsert(report));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReport(@PathVariable Long id) {
        reportService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/showAvailable")
    public ResponseEntity<AvailableToReportAnimalsDTO> showAvailableToReportAnimals(@RequestParam(required = false) Long reserveId, @RequestParam String worker) {
        return ResponseEntity.ok(reportService.getAvailableToReportAnimals(reserveId, worker));
    }

    @PostMapping("/history")
    public ResponseEntity<List<Report>> reportHistory(@RequestBody ReportHistoryParamsDTO params) {
        return ResponseEntity.ok(reportService.getReportHistory(params));
    }
}
