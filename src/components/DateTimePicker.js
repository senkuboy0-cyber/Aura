import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  Modal,
  Platform,
} from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { COLORS } from '../constants/theme';

export default function DateTimePicker({ label, value, onChange, minDate, disabled }) {
  const [showPicker, setShowPicker] = useState(false);
  const [tempDate, setTempDate] = useState(value || minDate || new Date());

  const formatDate = (date) => {
    if (!date) return null;
    return date.toLocaleDateString('en-US', {
      weekday: 'short',
      month: 'short',
      day: 'numeric',
      year: 'numeric',
    });
  };

  const formatTime = (date) => {
    if (!date) return null;
    return date.toLocaleTimeString('en-US', {
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  const handleClear = () => {
    onChange(null);
    setShowPicker(false);
  };

  const handleSetDate = () => {
    onChange(tempDate);
    setShowPicker(false);
  };

  const adjustDate = (field, amount) => {
    const newDate = new Date(tempDate);
    switch (field) {
      case 'day':
        newDate.setDate(newDate.getDate() + amount);
        break;
      case 'month':
        newDate.setMonth(newDate.getMonth() + amount);
        break;
      case 'year':
        newDate.setFullYear(newDate.getFullYear() + amount);
        break;
    }
    setTempDate(newDate);
  };

  const adjustTime = (field, amount) => {
    const newDate = new Date(tempDate);
    if (field === 'hour') {
      newDate.setHours((newDate.getHours() + amount + 24) % 24);
    } else if (field === 'minute') {
      newDate.setMinutes((newDate.getMinutes() + amount + 60) % 60);
    }
    setTempDate(newDate);
  };

  return (
    <View style={styles.container}>
      <Text style={styles.label}>{label}</Text>
      <TouchableOpacity
        style={[styles.dateButton, disabled && styles.disabled]}
        onPress={() => !disabled && setShowPicker(true)}
        disabled={disabled}
      >
        <Ionicons
          name={label === 'Reminder' ? 'alarm-outline' : 'calendar-outline'}
          size={20}
          color={value ? COLORS.primary : COLORS.textSecondary}
        />
        <Text style={[styles.dateText, value && styles.dateTextActive]}>
          {value ? `${formatDate(value)}${formatTime(value) ? ` at ${formatTime(value)}` : ''}` : `Set ${label.toLowerCase()}`}
        </Text>
        {value && (
          <TouchableOpacity onPress={handleClear} style={styles.clearButton}>
            <Ionicons name="close-circle" size={20} color={COLORS.textSecondary} />
          </TouchableOpacity>
        )}
      </TouchableOpacity>

      <Modal
        visible={showPicker}
        animationType="slide"
        transparent
        onRequestClose={() => setShowPicker(false)}
      >
        <View style={styles.modalOverlay}>
          <View style={styles.modalContent}>
            <View style={styles.modalHeader}>
              <Text style={styles.modalTitle}>Select {label}</Text>
              <TouchableOpacity onPress={() => setShowPicker(false)}>
                <Ionicons name="close" size={24} color={COLORS.text} />
              </TouchableOpacity>
            </View>

            {/* Date Selector */}
            <View style={styles.pickerSection}>
              <Text style={styles.sectionLabel}>Date</Text>
              <View style={styles.pickerRow}>
                <View style={styles.pickerColumn}>
                  <Text style={styles.pickerLabel}>Day</Text>
                  <View style={styles.pickerControl}>
                    <TouchableOpacity onPress={() => adjustDate('day', 1)} style={styles.pickerButton}>
                      <Ionicons name="chevron-up" size={20} color={COLORS.primary} />
                    </TouchableOpacity>
                    <Text style={styles.pickerValue}>{tempDate.getDate()}</Text>
                    <TouchableOpacity onPress={() => adjustDate('day', -1)} style={styles.pickerButton}>
                      <Ionicons name="chevron-down" size={20} color={COLORS.primary} />
                    </TouchableOpacity>
                  </View>
                </View>

                <View style={styles.pickerColumn}>
                  <Text style={styles.pickerLabel}>Month</Text>
                  <View style={styles.pickerControl}>
                    <TouchableOpacity onPress={() => adjustDate('month', 1)} style={styles.pickerButton}>
                      <Ionicons name="chevron-up" size={20} color={COLORS.primary} />
                    </TouchableOpacity>
                    <Text style={styles.pickerValue}>{tempDate.toLocaleString('default', { month: 'short' })}</Text>
                    <TouchableOpacity onPress={() => adjustDate('month', -1)} style={styles.pickerButton}>
                      <Ionicons name="chevron-down" size={20} color={COLORS.primary} />
                    </TouchableOpacity>
                  </View>
                </View>

                <View style={styles.pickerColumn}>
                  <Text style={styles.pickerLabel}>Year</Text>
                  <View style={styles.pickerControl}>
                    <TouchableOpacity onPress={() => adjustDate('year', 1)} style={styles.pickerButton}>
                      <Ionicons name="chevron-up" size={20} color={COLORS.primary} />
                    </TouchableOpacity>
                    <Text style={styles.pickerValue}>{tempDate.getFullYear()}</Text>
                    <TouchableOpacity onPress={() => adjustDate('year', -1)} style={styles.pickerButton}>
                      <Ionicons name="chevron-down" size={20} color={COLORS.primary} />
                    </TouchableOpacity>
                  </View>
                </View>
              </View>
            </View>

            {/* Time Selector */}
            <View style={styles.pickerSection}>
              <Text style={styles.sectionLabel}>Time</Text>
              <View style={styles.pickerRow}>
                <View style={styles.pickerColumn}>
                  <Text style={styles.pickerLabel}>Hour</Text>
                  <View style={styles.pickerControl}>
                    <TouchableOpacity onPress={() => adjustTime('hour', 1)} style={styles.pickerButton}>
                      <Ionicons name="chevron-up" size={20} color={COLORS.primary} />
                    </TouchableOpacity>
                    <Text style={styles.pickerValue}>
                      {tempDate.getHours().toString().padStart(2, '0')}
                    </Text>
                    <TouchableOpacity onPress={() => adjustTime('hour', -1)} style={styles.pickerButton}>
                      <Ionicons name="chevron-down" size={20} color={COLORS.primary} />
                    </TouchableOpacity>
                  </View>
                </View>

                <Text style={styles.timeSeparator}>:</Text>

                <View style={styles.pickerColumn}>
                  <Text style={styles.pickerLabel}>Minute</Text>
                  <View style={styles.pickerControl}>
                    <TouchableOpacity onPress={() => adjustTime('minute', 15)} style={styles.pickerButton}>
                      <Ionicons name="chevron-up" size={20} color={COLORS.primary} />
                    </TouchableOpacity>
                    <Text style={styles.pickerValue}>
                      {tempDate.getMinutes().toString().padStart(2, '0')}
                    </Text>
                    <TouchableOpacity onPress={() => adjustTime('minute', -15)} style={styles.pickerButton}>
                      <Ionicons name="chevron-down" size={20} color={COLORS.primary} />
                    </TouchableOpacity>
                  </View>
                </View>
              </View>
            </View>

            {/* Action Buttons */}
            <View style={styles.modalButtons}>
              <TouchableOpacity style={styles.cancelButton} onPress={() => setShowPicker(false)}>
                <Text style={styles.cancelButtonText}>Cancel</Text>
              </TouchableOpacity>
              <TouchableOpacity style={styles.confirmButton} onPress={handleSetDate}>
                <Text style={styles.confirmButtonText}>Confirm</Text>
              </TouchableOpacity>
            </View>
          </View>
        </View>
      </Modal>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    marginBottom: 20,
  },
  label: {
    fontSize: 16,
    fontWeight: '600',
    color: COLORS.text,
    marginBottom: 12,
  },
  dateButton: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: COLORS.card,
    padding: 16,
    borderRadius: 12,
    borderWidth: 1,
    borderColor: COLORS.border,
    gap: 12,
  },
  dateText: {
    fontSize: 16,
    color: COLORS.textSecondary,
    flex: 1,
  },
  dateTextActive: {
    color: COLORS.text,
  },
  clearButton: {
    padding: 4,
  },
  disabled: {
    opacity: 0.5,
  },
  modalOverlay: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,0.5)',
    justifyContent: 'flex-end',
  },
  modalContent: {
    backgroundColor: COLORS.card,
    borderTopLeftRadius: 24,
    borderTopRightRadius: 24,
    padding: 24,
    paddingBottom: 40,
  },
  modalHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 24,
  },
  modalTitle: {
    fontSize: 20,
    fontWeight: 'bold',
    color: COLORS.text,
  },
  pickerSection: {
    marginBottom: 24,
  },
  sectionLabel: {
    fontSize: 14,
    fontWeight: '600',
    color: COLORS.textSecondary,
    marginBottom: 12,
    textTransform: 'uppercase',
  },
  pickerRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 12,
  },
  pickerColumn: {
    alignItems: 'center',
  },
  pickerLabel: {
    fontSize: 12,
    color: COLORS.textSecondary,
    marginBottom: 8,
  },
  pickerControl: {
    backgroundColor: COLORS.background,
    borderRadius: 12,
    padding: 8,
    alignItems: 'center',
  },
  pickerButton: {
    padding: 8,
  },
  pickerValue: {
    fontSize: 18,
    fontWeight: 'bold',
    color: COLORS.text,
    paddingHorizontal: 16,
    paddingVertical: 8,
    minWidth: 60,
    textAlign: 'center',
  },
  timeSeparator: {
    fontSize: 24,
    fontWeight: 'bold',
    color: COLORS.text,
  },
  modalButtons: {
    flexDirection: 'row',
    gap: 12,
    marginTop: 16,
  },
  cancelButton: {
    flex: 1,
    padding: 16,
    borderRadius: 12,
    backgroundColor: COLORS.background,
    alignItems: 'center',
  },
  cancelButtonText: {
    fontSize: 16,
    fontWeight: '600',
    color: COLORS.textSecondary,
  },
  confirmButton: {
    flex: 1,
    padding: 16,
    borderRadius: 12,
    backgroundColor: COLORS.primary,
    alignItems: 'center',
  },
  confirmButtonText: {
    fontSize: 16,
    fontWeight: '600',
    color: '#fff',
  },
});
