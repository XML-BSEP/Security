import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { Template } from 'src/app/model/certificates/Template';
import { UserTemplate } from 'src/app/model/user/userTemplate';
import { TemplateService } from 'src/app/service/template/template.service';

@Component({
  selector: 'app-saved-templates-dialog',
  templateUrl: './saved-templates-dialog.component.html',
  styleUrls: ['./saved-templates-dialog.component.css']
})
export class SavedTemplatesDialogComponent implements OnInit {
  savedTemplates : Template[] = [];
  user : Number
  constructor(public dialogRef: MatDialogRef<SavedTemplatesDialogComponent>, public templateService : TemplateService) { }

  ngOnInit(): void {
    this.user = Number(localStorage.getItem('userId'))
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
    this.templateService.getAllByUser(this.user).subscribe(data =>
      {
        this.savedTemplates = data;
        console.log(this.savedTemplates);
      });
  }
}
