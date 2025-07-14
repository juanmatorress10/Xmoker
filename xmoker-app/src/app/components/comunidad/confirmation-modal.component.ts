// src/app/shared/confirmation-modal.component.ts
import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  standalone: true,
  selector: 'app-confirmation-modal',
  template: `
    <div
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
    >
      <div class="bg-white rounded-lg shadow-lg w-11/12 max-w-md p-6">
        <h3 class="text-xl font-semibold mb-4">{{ title }}</h3>
        <p class="text-gray-700 mb-6">{{ message }}</p>
        <div class="flex justify-end space-x-3">
          <button
            class="px-4 py-2 rounded bg-gray-200 hover:bg-gray-300"
            (click)="cancel.emit()"
          >
            {{ cancelText }}
          </button>
          <button
            class="px-4 py-2 rounded bg-blue-600 text-white hover:bg-blue-700"
            (click)="confirm.emit()"
          >
            {{ confirmText }}
          </button>
        </div>
      </div>
    </div>
  `,
})
export class ConfirmationModalComponent {
  @Input() title = 'Confirmar';
  @Input() message = '¿Estás seguro?';
  @Input() confirmText = 'Sí';
  @Input() cancelText = 'Cancelar';

  @Output() confirm = new EventEmitter<void>();
  @Output() cancel = new EventEmitter<void>();
}
