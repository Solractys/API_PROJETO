package br.com.projeto.API_PROJETO.controller;

import br.com.projeto.API_PROJETO.entidade.Question;
import br.com.projeto.API_PROJETO.service.QuestionService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping
    public ResponseEntity<List<Question>> listar() {
        List<Question> question = questionService.listAll();
        return ResponseEntity.ok(question);
    }

    @PostMapping
    public ResponseEntity<Question> inserir(@RequestBody Question question ) {
        questionService.inserir(question);

        return ResponseEntity.created(null).body(question);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateField(
            @PathVariable String id,
            @RequestBody Map<String, String> updateFields) {
        try {
            ObjectId objectId = new ObjectId(id);
            String chave = updateFields.get("chave");
            String valor = updateFields.get("valor");

            if (chave == null || valor == null) {
                return ResponseEntity.badRequest().body("Missing required fields: 'chave' or 'valor'");
            }

            boolean success = questionService.updateField(objectId.toString(), chave, valor);
            if (success) {
                return ResponseEntity.ok("Updated successfully");
            } else {
                return ResponseEntity.badRequest().body("Failed to update field");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid ObjectId format");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable String id) {
        try {
            ObjectId objectId = new ObjectId(id);
            boolean success = questionService.deleteQuestion(objectId.toString());
            if (success) {
                return ResponseEntity.ok("Question deleted successfully");
            } else {
                return ResponseEntity.badRequest().body("Failed to delete question");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid ObjectId format");
        }
    }
}
