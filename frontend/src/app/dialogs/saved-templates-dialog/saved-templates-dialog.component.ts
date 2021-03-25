import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { Template } from 'src/app/model/certificates/Template';
import { TemplateService } from 'src/app/service/template/template.service';

@Component({
  selector: 'app-saved-templates-dialog',
  templateUrl: './saved-templates-dialog.component.html',
  styleUrls: ['./saved-templates-dialog.component.css']
})
export class SavedTemplatesDialogComponent implements OnInit {
  savedTemplates : Template[] = [];
  constructor(public dialogRef: MatDialogRef<SavedTemplatesDialogComponent>, public templateService : TemplateService) { }

  ngOnInit(): void {
    this.loadSavedTemplates();
  }
  onSubmit(template) {
    console.log(template);
    this.dialogRef.close({ template });
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  loadSavedTemplates(){
    this.templateService.getAll().subscribe(data =>
      {
        this.savedTemplates = data;
        console.log(this.savedTemplates);
      });
  }
}
