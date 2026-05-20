<template>
  <div>
    <el-card>
      <div slot="header"><span>预约咨询</span></div>
      <div class="booking-layout">
        <!-- 左侧日历 -->
        <div class="calendar-col">
          <div class="calendar-nav">
            <el-button icon="el-icon-arrow-left" circle size="mini" @click="prevMonth"></el-button>
            <span class="calendar-month">{{ currentYear }}年{{ currentMonth + 1 }}月</span>
            <el-button icon="el-icon-arrow-right" circle size="mini" @click="nextMonth"></el-button>
          </div>
          <div class="weekdays">
            <span v-for="d in ['日','一','二','三','四','五','六']" :key="d">{{ d }}</span>
          </div>
          <div class="calendar-grid">
            <div class="day-cell empty" v-for="n in firstDayOfWeek" :key="'e'+n"></div>
            <div
              class="day-cell"
              v-for="day in daysInMonth"
              :key="day.dateStr"
              :class="{
                'has-slots': day.hasSlots,
                selected: selectedDate === day.dateStr,
                past: day.isPast,
                today: day.isToday
              }"
              @click="selectDate(day)"
            >
              <span class="day-num">{{ day.day }}</span>
              <span v-if="day.hasSlots" class="slot-badge">{{ day.slotCount }}</span>
            </div>
          </div>
        </div>

        <!-- 右侧时段+表单 -->
        <div class="right-col">
          <!-- 时段列表 -->
          <div class="slot-section">
            <div class="section-title">
              {{ selectedDateDisplay || '选择日期' }}
              <span class="sub-title">可选时段</span>
            </div>
            <div class="slot-list" v-if="selectedDateSlots.length">
              <div
                class="slot-item"
                v-for="slot in selectedDateSlots"
                :key="slot.scheduleId"
                :class="{ selected: selectedSlot && selectedSlot.scheduleId === slot.scheduleId, full: isFull(slot) }"
                @click="selectSlot(slot)"
              >
                <span class="slot-time">{{ slot.startTime }}-{{ slot.endTime }}</span>
                <span class="slot-teacher">{{ getTeacherName(slot.teacherId) }}</span>
                <span class="slot-loc">{{ slot.location }}</span>
                <span class="slot-avail" :class="{ full: isFull(slot) }">余{{ slot.maxCount - slot.bookedCount }}</span>
                <span class="full-tag" v-if="isFull(slot)">已满</span>
              </div>
            </div>
            <div v-else-if="!selectedDate" class="empty-msg">点击左侧日期查看时段</div>
            <div v-else class="empty-msg">该日期暂无时段</div>
          </div>

          <!-- 预约表单 -->
          <div class="form-section" v-if="selectedSlot">
            <el-input
              type="textarea"
              :rows="2"
              v-model="form.reason"
              placeholder="请描述您想咨询的问题..."
            />
            <div class="submit-bar">
              <el-button type="primary" size="small" @click="handleBook">提交预约</el-button>
              <span class="submit-tip">{{ selectedSlot.startTime }}-{{ selectedSlot.endTime }} · {{ selectedSlot.location }} · {{ getTeacherName(selectedSlot.teacherId) }}</span>
            </div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import * as api from '@/api/schedule'
import * as bookingApi from '@/api/consultation'
export default {
  data() {
    return {
      schedules: [],
      selectedDate: '',
      selectedSlot: null,
      form: { reason: '', studentId: null },
      currentDate: new Date(),
      dateSlotMap: {}
    }
  },
  computed: {
    currentYear() { return this.currentDate.getFullYear() },
    currentMonth() { return this.currentDate.getMonth() },
    daysInMonth() {
      const year = this.currentYear
      const month = this.currentMonth
      const days = new Date(year, month + 1, 0).getDate()
      const result = []
      const today = new Date()
      today.setHours(0, 0, 0, 0)
      for (let d = 1; d <= days; d++) {
        const date = new Date(year, month, d)
        const dateStr = `${year}-${String(month + 1).padStart(2, '0')}-${String(d).padStart(2, '0')}`
        const slots = this.dateSlotMap[dateStr] || []
        result.push({
          day: d,
          dateStr,
          hasSlots: slots.length > 0,
          slotCount: slots.length,
          slots,
          isPast: date < today,
          isToday: date.toDateString() === today.toDateString()
        })
      }
      return result
    },
    firstDayOfWeek() {
      return new Date(this.currentYear, this.currentMonth, 1).getDay()
    },
    selectedDateSlots() {
      if (!this.selectedDate) return []
      return this.dateSlotMap[this.selectedDate] || []
    },
    selectedDateDisplay() {
      if (!this.selectedDate) return ''
      const d = new Date(this.selectedDate)
      const weekDays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
      return `${d.getMonth() + 1}月${d.getDate()}日 ${weekDays[d.getDay()]}`
    }
  },
  created() {
    const user = this.$store.state.user
    if (user) this.form.studentId = user.userId
    api.getOpenSchedules().then(r => {
      this.schedules = (r.data && r.data.records) || []
      const map = {}
      for (const s of this.schedules) {
        if (!map[s.scheduleDate]) map[s.scheduleDate] = []
        map[s.scheduleDate].push(s)
      }
      this.dateSlotMap = map
      const firstDate = Object.keys(map).sort()[0]
      if (firstDate) this.selectedDate = firstDate
    }).catch(() => this.$message.error('加载时段数据失败'))
  },
  methods: {
    prevMonth() {
      this.currentDate = new Date(this.currentYear, this.currentMonth - 1, 1)
    },
    nextMonth() {
      this.currentDate = new Date(this.currentYear, this.currentMonth + 1, 1)
    },
    selectDate(day) {
      if (!day.hasSlots) return
      this.selectedDate = day.dateStr
      this.selectedSlot = null
    },
    selectSlot(slot) {
      if (this.isFull(slot)) return
      if (this.selectedSlot && this.selectedSlot.scheduleId === slot.scheduleId) {
        this.selectedSlot = null
      } else {
        this.selectedSlot = slot
      }
    },
    isFull(slot) {
      return slot.bookedCount >= slot.maxCount
    },
    getTeacherName(teacherId) {
      const slot = this.schedules.find(s => s.teacherId === teacherId)
      return (slot && slot.teacherName) || '心理教师'
    },
    handleBook() {
      if (!this.selectedSlot) { this.$message.error('请选择时段'); return }
      if (!this.form.reason) { this.$message.error('请填写预约原因'); return }
      this.form.scheduleId = this.selectedSlot.scheduleId
      bookingApi.bookConsultation(this.form).then(() => {
        this.$message.success('预约成功')
        this.$router.push('/student/booking')
      }).catch(() => this.$message.error('预约失败'))
    }
  }
}
</script>

<style scoped>
.booking-layout {
  display: flex;
  gap: 20px;
  max-height: 500px;
}

/* ===== 左侧日历 ===== */
.calendar-col {
  width: 260px;
  flex-shrink: 0;
}

.calendar-nav {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin-bottom: 8px;
}

.calendar-month {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  min-width: 100px;
  text-align: center;
}

.weekdays {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  margin-bottom: 2px;
}

.weekdays span {
  text-align: center;
  font-size: 12px;
  color: #909399;
  font-weight: 500;
  padding: 2px 0;
}

.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 2px;
}

.day-cell {
  aspect-ratio: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.15s;
  border: 1px solid transparent;
  position: relative;
}

.day-cell.empty { cursor: default; }

.day-cell.has-slots:not(.past):not(.selected) {
  border-color: #d9ecff;
  background: #ecf5ff;
  color: #409EFF;
}

.day-cell.has-slots:not(.past):not(.selected):hover {
  border-color: #409EFF;
}

.day-cell.selected {
  border-color: #409EFF;
  background: #409EFF;
  color: #fff;
}

.day-cell.past {
  color: #c0c4cc;
  cursor: not-allowed;
}

.day-cell.today:not(.selected) { border-color: #E6A23C; }

.day-num { font-size: 13px; font-weight: 600; line-height: 1; }

.slot-badge {
  position: absolute;
  bottom: 1px;
  right: 2px;
  font-size: 9px;
  line-height: 1;
  color: #409EFF;
}

.day-cell.selected .slot-badge { color: #fff; }

/* ===== 右侧 ===== */
.right-col {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.section-title {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 10px;
  display: flex;
  align-items: baseline;
  gap: 8px;
}

.sub-title {
  font-weight: 400;
  font-size: 13px;
  color: #909399;
}

.slot-section {
  flex: 1;
  overflow-y: auto;
  margin-bottom: 12px;
}

.slot-list {
  display: flex;
  flex-direction: column;
  gap: 6px;
  max-height: 300px;
  overflow-y: auto;
}

.slot-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 14px;
  border: 1px solid #ebeef5;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.15s;
  font-size: 13px;
  position: relative;
}

.slot-item:hover:not(.full) {
  border-color: #409EFF;
  background: #fafcff;
}

.slot-item.selected {
  border-color: #409EFF;
  background: #ecf5ff;
}

.slot-item.full {
  opacity: 0.5;
  cursor: not-allowed;
}

.slot-time {
  font-weight: 600;
  color: #409EFF;
  white-space: nowrap;
  min-width: 110px;
}

.slot-teacher { white-space: nowrap; color: #303133; }
.slot-loc { white-space: nowrap; color: #606266; }

.slot-avail {
  margin-left: auto;
  color: #67C23A;
  font-weight: 500;
  font-size: 12px;
}

.slot-avail.full { color: #F56C6C; }

.full-tag {
  position: absolute;
  top: 4px;
  right: 8px;
  background: #F56C6C;
  color: #fff;
  font-size: 10px;
  padding: 1px 6px;
  border-radius: 8px;
}

.empty-msg {
  text-align: center;
  color: #c0c4cc;
  padding: 40px 0;
  font-size: 14px;
}

/* ===== 表单 ===== */
.form-section {
  border-top: 1px solid #ebeef5;
  padding-top: 12px;
}

.submit-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-top: 8px;
}

.submit-tip {
  font-size: 12px;
  color: #909399;
}
</style>
