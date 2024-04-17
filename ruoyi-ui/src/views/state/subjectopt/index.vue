<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['state:subject:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['state:subject:edit']"
        >修改</el-button>
      </el-col>
    </el-row>
    <el-table v-loading="loading" :data="subjectList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主题名称" align="center" prop="subjectName" />
      <el-table-column label="开始日期" align="center" prop="beginDate" >
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.beginDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="截止日期" align="center" prop="endDate" >
        <template v-slot="scope">
          <span>{{ parseTime(scope.row.endDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['state:subject:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleRemove(scope.row)"
            v-hasPermi="['state:subject:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改用户信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="主题名称" prop="subjectName">
          <el-input v-model="form.subjectName" placeholder="请输入主题名称" />
        </el-form-item>
        <el-form-item label="开始日期" prop="beginDate">
          <el-date-picker clearable
                          v-model="form.beginDate"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择最后登录时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="截止日期" prop="endDate">
          <el-date-picker clearable
                          v-model="form.endDate"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择最后登录时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

import { listSubject, addOrUpdateSubject, removeSubject } from "@/api/state/subjectopt";

export default {
  name: "Subject",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 用户信息表格数据
      subjectList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        subjectName: null,
        beginDate: null,
        endDate: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        subjectName: [
          { required: true, message: "主题名称不能为空", trigger: "blur" }
        ],
        beginDate: [
          { required: true, message: "开始日期不能为空", trigger: "blur" }
        ],
        endDate: [
          { required: true, message: "截止日期不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listSubject(this.queryParams).then(response => {
        this.subjectList = response.data
        this.loading = false
      })
    },
    //取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    //重置
    reset() {
      this.form = {
        id: null,
        subjectName: null,
        beginDate: null,
        endDate: null
      }
      this.resetForm("form")
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          addOrUpdateSubject(this.form).then(response => {
            this.$modal.msgSuccess("新增或修改成功")
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加投票信息"
    },
    handleUpdate(row) {
      this.reset()
      if (this.ids && this.ids.length > 1) {
        this.$modal.msgError("请选择一条数据")
        return
      }
      const id = row.id || this.ids[0]
      let secSub = this.subjectList.filter(item => item.id === id)
      if (secSub && secSub.length > 0){
        this.form = secSub[0]
      }
      this.open = true
      this.title = "修改投票信息"
    },
    handleRemove(row) {

      this.$confirm("确认是否删除该数据？").then(() => {

      })

    },
    delSubject() {
      if (! row.id){
        this.$modal.msgError("数据异常")
        return
      }
      removeSubject(row.id).then(response => {
        this.$modal.msgSuccess("删除成功")
        this.getList()
      })
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleQuery() {
      this.getList()
    },
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    }
  }
}
</script>


<style scoped lang="scss">

</style>
