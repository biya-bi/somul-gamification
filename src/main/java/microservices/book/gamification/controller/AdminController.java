/**
 * 
 */
package microservices.book.gamification.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import microservices.book.gamification.service.AdminService;

/**
 * @author biya-bi
 *
 */
@Profile("test")
@RestController
@RequestMapping("/gamification/admin")
class AdminController {
	private final AdminService adminService;

	AdminController(final AdminService adminService) {
		this.adminService = adminService;
	}

	@PostMapping("/delete-db")
	ResponseEntity<?> deleteDatabase() {
		adminService.deleteDatabaseContents();
		return ResponseEntity.ok().build();
	}
}
