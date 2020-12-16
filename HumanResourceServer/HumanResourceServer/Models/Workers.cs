using System;
using System.Collections.Generic;

namespace HumanResourceServer.Models
{
    public partial class Workers
    {
        public int Id { get; set; }
        public string Login { get; set; }
        public string Password { get; set; }
        public string Name { get; set; }
        public string Surname { get; set; }
        public string Sex { get; set; }
        public string Department { get; set; }
        public string Position { get; set; }
        public string Salt { get; set; }

        public Departments DepartmentNavigation { get; set; }
    }
}
